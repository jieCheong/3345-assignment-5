import java.util.Scanner;

public class EmployeeHeap {

    // Employee record 
    static class Employee {
        int    empNum;
        double hourlyRate;

        Employee(int empNum, double hourlyRate) {
            this.empNum     = empNum;
            this.hourlyRate = hourlyRate;
        }

        public String toString() {
            return "EmpNum: " + empNum + "  Rate: $" + hourlyRate;
        }
    }

    // Min-Heap (keyed on empNum) 
    static Employee[] heap = new Employee[100];
    static int size = 0;

    // Heap helpers 
    static void swap(int i, int j) {
        Employee tmp = heap[i]; heap[i] = heap[j]; heap[j] = tmp;
    }

    static void heapifyUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[parent].empNum > heap[i].empNum) {
                swap(i, parent);
                i = parent;
            } else break;
        }
    }

    static void heapifyDown(int i) {
        while (true) {
            int left  = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left  < size && heap[left].empNum  < heap[smallest].empNum) smallest = left;
            if (right < size && heap[right].empNum < heap[smallest].empNum) smallest = right;

            if (smallest != i) { swap(i, smallest); i = smallest; }
            else break;
        }
    }

    // Insert 
    static void insert(Employee e) {
        heap[size++] = e;
        heapifyUp(size - 1);
        System.out.println("Inserted: " + e);
    }

    // Delete by empNum
    static void delete(int empNum) {
        int idx = -1;
        for (int i = 0; i < size; i++) {
            if (heap[i].empNum == empNum) { idx = i; break; }
        }
        if (idx == -1) { System.out.println("Employee " + empNum + " not found."); return; }

        heap[idx] = heap[--size];   // replace with last element
        heap[size] = null;
        if (idx < size) {
            heapifyUp(idx);
            heapifyDown(idx);
        }
        System.out.println("Deleted employee #" + empNum);
    }

    // Heap Sort (sorts a COPY, leaves heap intact) 
    static void heapSort() {
        // Copy heap into a temporary array
        Employee[] arr = new Employee[size];
        for (int i = 0; i < size; i++) arr[i] = heap[i];
        int n = size;

        // Build max-heap on arr
        for (int i = n / 2 - 1; i >= 0; i--) maxHeapify(arr, n, i);

        // Extract elements one by one
        for (int i = n - 1; i > 0; i--) {
            Employee tmp = arr[0]; arr[0] = arr[i]; arr[i] = tmp;
            maxHeapify(arr, i, 0);
        }

        System.out.println("\n── Sorted Employee List (ascending by empNum) ──");
        for (Employee e : arr) System.out.println(e);
    }

    static void maxHeapify(Employee[] arr, int n, int i) {
        int largest = i, l = 2*i+1, r = 2*i+2;
        if (l < n && arr[l].empNum > arr[largest].empNum) largest = l;
        if (r < n && arr[r].empNum > arr[largest].empNum) largest = r;
        if (largest != i) {
            Employee tmp = arr[i]; arr[i] = arr[largest]; arr[largest] = tmp;
            maxHeapify(arr, n, largest);
        }
    }

    // Print current heap
    static void printHeap() {
        System.out.println("\n── Current Heap ──");
        for (int i = 0; i < size; i++) System.out.println(heap[i]);
    }

    // Main 
 public static void main(String[] args) throws Exception {
    // Load records from file if provided
    if (args.length > 0) {
        Scanner fileSc = new Scanner(new java.io.File(args[0]));
        while (fileSc.hasNextLine()) {
            String line = fileSc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            insert(new Employee(Integer.parseInt(parts[0]),
                                Double.parseDouble(parts[1])));
        }
        fileSc.close();
        System.out.println("Loaded " + size + " records from " + args[0]);
    }

    // Menu always reads from keyboard
    Scanner menuSc = new Scanner(System.in);
    while (true) {
        System.out.println("\n1) Insert  2) Delete  3) Sort & Display  4) Print Heap  5) Exit");
        System.out.print("Choice: ");
        int choice = Integer.parseInt(menuSc.nextLine().trim());

        if (choice == 1) {
            System.out.print("Enter empNum hourlyRate: ");
            String[] p = menuSc.nextLine().trim().split("\\s+");
            insert(new Employee(Integer.parseInt(p[0]), Double.parseDouble(p[1])));
        } else if (choice == 2) {
            System.out.print("Enter empNum to delete: ");
            delete(Integer.parseInt(menuSc.nextLine().trim()));
        } else if (choice == 3) {
            heapSort();
        } else if (choice == 4) {
            printHeap();
        } else {
            System.out.println("Goodbye!");
            break;
        }
    }
    menuSc.close();
}
}