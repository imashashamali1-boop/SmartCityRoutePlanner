import java.util.*;
// =======================================
// Reg.Num.- 22ug3-806 - GRAPH DATA STRUCTURE (Adjacency List)
// =======================================
class Graph {
    private Map<String, List<String>> adjlist = new HashMap<>();

    // Add new location (vertex)
    public void addLocation(String location) {
        adjlist.putIfAbsent(location, new ArrayList<>());
        System.out.println(location + " added to the city map.");
    }

    // Remove location
    public void removeLocation(String location) {
        if (!adjlist.containsKey(location)) {
            System.out.println("Location not found.");
            return;
        }
        adjlist.remove(location);
        for (List<String> roads : adjlist.values()) {
            roads.remove(location);
        }
        System.out.println(location + " removed from the city map.");
    }

    // Add road (edge)
    public void addRoad(String src, String dest) {
        if (adjlist.containsKey(src) && adjlist.containsKey(dest)) {
            adjlist.get(src).add(dest);
            adjlist.get(dest).add(src); // Undirected graph
            System.out.println("Road added between " + src + " and " + dest);
        } else {
            System.out.println("One or both locations not found.");
        }
    }

    // Remove road (edge)
    public void removeRoad(String src, String dest) {
        if (adjlist.containsKey(src) && adjlist.containsKey(dest)) {
            adjlist.get(src).remove(dest);
            adjlist.get(dest).remove(src);
            System.out.println("Road removed between " + src + " and " + dest);
        } else {
            System.out.println("Invalid road or locations not found.");
        }
    }

    // Display all connections
    public void displayConnections() {
        if (adjlist.isEmpty()) {
            System.out.println("No locations or roads found.");
            return;
        }
        System.out.println("\nCity Connections:");
        for (String loc : adjlist.keySet()) {
            System.out.println(" " + loc + " -> " + adjlist.get(loc));
        }
    }

    // Return all location names
    public Set<String> getLocations() {
        return adjlist.keySet();
    }
}

// =======================================
// Reg.Num.- 22ug3-0476 - AVL TREE STRUCTURE (for Location Data)
// =======================================
class AVLNode {
    String data;
    AVLNode left, right;
    int height;

    AVLNode(String d) {
        data = d;
        height = 1;
    }
}

class AVLTree {
    private AVLNode root;

    int height(AVLNode n) {
        return (n == null) ? 0 : n.height;
    }

    int getBalance(AVLNode n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    AVLNode insert(AVLNode node, String data) {
        if (node == null) return new AVLNode(data);

        if (data.compareTo(node.data) < 0)
            node.left = insert(node.left, data);
        else if (data.compareTo(node.data) > 0)
            node.right = insert(node.right, data);
        else
            return node; // duplicates not allowed

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Balance the tree
        if (balance > 1 && data.compareTo(node.left.data) < 0)
            return rightRotate(node);
        if (balance < -1 && data.compareTo(node.right.data) > 0)
            return leftRotate(node);
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    void insert(String data) {
        root = insert(root, data);
    }

    void inOrder(AVLNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.data + " ");
            inOrder(node.right);
        }
    }

    void display() {
        System.out.print("\nLocations (in sorted order): ");
        inOrder(root);
        System.out.println();
    }
}

// =======================================
// Reg.Num.- 22ug3-0681 - MENU, INPUT, AND PROGRAM INTEGRATION
// =======================================
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Graph cityGraph = new Graph();
        AVLTree locationTree = new AVLTree();

        while (true) {
            System.out.println("\n--- Smart City Route Planner ---");
            System.out.println("1. Add a new location");
            System.out.println("2. Remove a location");
            System.out.println("3. Add a road between locations");
            System.out.println("4. Remove a road");
            System.out.println("5. Display all connections");
            System.out.println("6. Display all locations (AVL Tree)");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine(); // consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter location name: ");
                    String loc = sc.nextLine();
                    cityGraph.addLocation(loc);
                    locationTree.insert(loc);
                    break;

                case 2:
                    System.out.print("Enter location to remove: ");
                    String rem = sc.nextLine();
                    cityGraph.removeLocation(rem);
                    System.out.println("Note: AVL tree removal not implemented for simplicity.");
                    break;

                case 3:
                    System.out.print("Enter source location: ");
                    String src = sc.nextLine();
                    System.out.print("Enter destination location: ");
                    String dest = sc.nextLine();
                    cityGraph.addRoad(src, dest);
                    break;

                case 4:
                    System.out.print("Enter source location: ");
                    src = sc.nextLine();
                    System.out.print("Enter destination location: ");
                    dest = sc.nextLine();
                    cityGraph.removeRoad(src, dest);
                    break;

                case 5:
                    cityGraph.displayConnections();
                    break;

                case 6:
                    locationTree.display();
                    break;

                case 7:
                    System.out.println("Exiting program... Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }
}