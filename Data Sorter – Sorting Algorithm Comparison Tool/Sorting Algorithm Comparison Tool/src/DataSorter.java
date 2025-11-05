import java.util.*;

public class DataSorter {

    // OperationCounter is a simple wrapper so we can pass by reference
    static class OperationCounter {
        long count = 0;
        void inc() { count++; }
        void add(long v) { count += v; }
        long get() { return count; }
    }

    // --- 22ug3-0476- Bubble Sort (counts comparisons + swaps as operations) ---
    public static int[] bubbleSort(int[] arr, OperationCounter counter) {
        int n = arr.length;
        int[] a = Arrays.copyOf(arr, n);
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                counter.inc(); // comparison
                if (a[j] > a[j+1]) {
                    // swap counts as 3 assignments (temp, a[j], a[j+1]) or count as 1 swap — we'll count as 3
                    int tmp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = tmp;
                    counter.add(3);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        return a;
    }

    // ---22ug3-0806-  Merge Sort ---
    public static int[] mergeSort(int[] arr, OperationCounter counter) {
        int[] a = Arrays.copyOf(arr, arr.length);
        mergeSortHelper(a, 0, a.length - 1, counter);
        return a;
    }

    private static void mergeSortHelper(int[] a, int left, int right, OperationCounter counter) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSortHelper(a, left, mid, counter);
        mergeSortHelper(a, mid + 1, right, counter);
        merge(a, left, mid, right, counter);
    }

    private static void merge(int[] a, int left, int mid, int right, OperationCounter counter) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];
        for (int i = 0; i < n1; i++) { L[i] = a[left + i]; counter.inc(); } // copy counts as assignment
        for (int j = 0; j < n2; j++) { R[j] = a[mid + 1 + j]; counter.inc(); }
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            counter.inc(); // comparison between L[i] and R[j]
            if (L[i] <= R[j]) {
                a[k++] = L[i++];
                counter.inc(); // assignment
            } else {
                a[k++] = R[j++];
                counter.inc(); // assignment
            }
        }
        while (i < n1) { a[k++] = L[i++]; counter.inc(); } // assignment
        while (j < n2) { a[k++] = R[j++]; counter.inc(); } // assignment
    }

    // ---22ug3-0681-  Quick Sort (Lomuto partition) ---
    public static int[] quickSort(int[] arr, OperationCounter counter) {
        int[] a = Arrays.copyOf(arr, arr.length);
        quickSortHelper(a, 0, a.length - 1, counter);
        return a;
    }

    private static void quickSortHelper(int[] a, int low, int high, OperationCounter counter) {
        if (low < high) {
            int p = partition(a, low, high, counter);
            quickSortHelper(a, low, p - 1, counter);
            quickSortHelper(a, p + 1, high, counter);
        }
    }

    private static int partition(int[] a, int low, int high, OperationCounter counter) {
        int pivot = a[high];
        counter.inc(); // reading pivot counts as one op
        int i = low - 1;
        for (int j = low; j <= high - 1; j++) {
            counter.inc(); // comparison a[j] <= pivot
            if (a[j] <= pivot) {
                i++;
                // swap a[i] and a[j]: count as 3 assignments
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
                counter.add(3);
            }
        }
        // final swap a[i+1] and a[high]
        int tmp = a[i+1];
        a[i+1] = a[high];
        a[high] = tmp;
        counter.add(3);
        return i + 1;
    }
//--- 22ug3-0681/ 22ug3-0806/ 22ug3-0476---

    // Utility: generate random array
    public static int[] generateRandom(int n, int min, int max) {
        Random r = new Random();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt(max - min + 1) + min;
        return a;
    }

    // Utility: print array (abbreviate if large)
    public static void printArray(int[] a) {
        int n = a.length;
        if (n <= 50) {
            System.out.println(Arrays.toString(a));
        } else {
            System.out.print("[");
            for (int i = 0; i < 10; i++) System.out.print(a[i] + (i==9 ? "" : ", "));
            System.out.print(", ..., ");
            for (int i = n - 10; i < n; i++) System.out.print(a[i] + (i==n-1 ? "" : ", "));
            System.out.println("]");
        }
    }

    // Clone helper
    public static int[] cloneArray(int[] a) { return Arrays.copyOf(a, a.length); }

    // Main menu & wiring
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] dataset = new int[0];
        while (true) {
            System.out.println("\n--- Data Sorter: Sorting Algorithm Comparison Tool ---");
            System.out.println("1. Enter numbers manually");
            System.out.println("2. Generate random numbers");
            System.out.println("3. Perform Bubble Sort");
            System.out.println("4. Perform Merge Sort");
            System.out.println("5. Perform Quick Sort");
            System.out.println("6. Compare all algorithms (show performance table)");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.println("Enter integers separated by spaces:");
                    String line = sc.nextLine().trim();
                    try {
                        String[] parts = line.split("\\s+");
                        dataset = new int[parts.length];
                        for (int i = 0; i < parts.length; i++) dataset[i] = Integer.parseInt(parts[i]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter integers only.");
                        dataset = new int[0];
                    }
                    break;
                case "2":
                    try {
                        System.out.print("How many numbers to generate? ");
                        int n = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Min value? ");
                        int min = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Max value? ");
                        int max = Integer.parseInt(sc.nextLine().trim());
                        dataset = generateRandom(n, min, max);
                        System.out.println("Generated dataset:");
                        printArray(dataset);
                    } catch (Exception e) {
                        System.out.println("Invalid input for generation. Aborting generation.");
                        dataset = new int[0];
                    }
                    break;
                case "3":
                    if (dataset.length == 0) { System.out.println("No dataset. Enter/generate data first."); break; }
                    runSingleSort("Bubble Sort", dataset, DataSorter::bubbleSort);
                    break;
                case "4":
                    if (dataset.length == 0) { System.out.println("No dataset. Enter/generate data first."); break; }
                    runSingleSort("Merge Sort", dataset, DataSorter::mergeSort);
                    break;
                case "5":
                    if (dataset.length == 0) { System.out.println("No dataset. Enter/generate data first."); break; }
                    runSingleSort("Quick Sort", dataset, DataSorter::quickSort);
                    break;
                case "6":
                    if (dataset.length == 0) { System.out.println("No dataset. Enter/generate data first."); break; }
                    compareAll(dataset);
                    break;
                case "7":
                    System.out.println("Exiting. Bye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
                    break;
            }
        }
    }

    // functional interface to pass sorting method
    @FunctionalInterface
    public interface SortFunction {
        int[] apply(int[] arr, OperationCounter counter);
    }

    // run single algorithm & print results
    public static void runSingleSort(String name, int[] dataset, SortFunction fn) {
        int[] input = cloneArray(dataset);
        OperationCounter counter = new OperationCounter();
        long t0 = System.nanoTime();
        int[] sorted = fn.apply(input, counter);
        long t1 = System.nanoTime();
        System.out.println("\n=== " + name + " ===");
        System.out.println("Time (ms): " + ((t1 - t0) / 1_000_000.0));
        System.out.println("Operation count: " + counter.get());
        System.out.println("Sorted output:");
        printArray(sorted);
    }

    // compare all three algorithms and show a table
    public static void compareAll(int[] dataset) {
        String[] names = { "Bubble Sort", "Merge Sort", "Quick Sort" };
        SortFunction[] fns = { DataSorter::bubbleSort, DataSorter::mergeSort, DataSorter::quickSort };
        double[] timesMs = new double[3];
        long[] ops = new long[3];
        int[][] results = new int[3][];
        for (int i = 0; i < fns.length; i++) {
            int[] input = cloneArray(dataset);
            OperationCounter counter = new OperationCounter();
            long t0 = System.nanoTime();
            int[] sorted = fns[i].apply(input, counter);
            long t1 = System.nanoTime();
            timesMs[i] = (t1 - t0) / 1_000_000.0;
            ops[i] = counter.get();
            results[i] = sorted;
        }
        // print table
        System.out.println("\nComparison Results:");
        System.out.printf("%-12s | %-12s | %-15s%n", "Algorithm", "Time (ms)", "Operation Count");
        System.out.println("-------------------------------------------------");
        for (int i = 0; i < names.length; i++) {
            System.out.printf("%-12s | %-12.4f | %-15d%n", names[i], timesMs[i], ops[i]);
        }
        // show sorted output from one algorithm (all should match)
        System.out.println("\nSorted output (from Merge Sort):");
        printArray(results[1]);
    }
}
