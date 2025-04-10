package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private Map<T, Integer> elementindex;
    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */
    // Initialize the disjoint set
    public UnionBySizeCompressingDisjointSets() {
        // Initialize the pointer list
        this.pointers = new ArrayList<>();
        // Initialize the mapping of the element to the index
        this.elementindex = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        // Map the element to the size of the current pointer list
        elementindex.put(item, pointers.size());
        // Add a new element to the pointer list
        pointers.add(-1);
    }

    // Find the root node of element i and perform path compression
    private int find(int i) {
        // Return the index of the current element if the current element is the root node
        if (pointers.get(i) < 0) {
            return i;
        }
        // Find the root node recursively
        int root = find(pointers.get(i));
        // Path compression
        pointers.set(i, root);
        return root;
    }

    @Override
    public int findSet(T item) {
        // Get the index of the element
        Integer index = elementindex.get(item);
        // Throw an exception if the element does not exist
        if (index == null) {
            throw new IllegalArgumentException("not exist");
        }
        return find(index);
    }

    @Override
    public boolean union(T item1, T item2) {
        // Get the index of the first element
        Integer index1 = elementindex.get(item1);
        // Get the index of the second element
        Integer index2 = elementindex.get(item2);
        // Throw an exception if any element does not exist
        if (index1 == null || index2 == null) {
            throw new IllegalArgumentException("Items not present in any set.");
        }
        // Find the root node of the first element
        int root1 = find(index1);
        // Find the root node of the second element
        int root2 = find(index2);
        // If both elements are already in the same set, return false
        if (root1 == root2) {
            return false;
        }
        // Get the size of the first collection
        int size1 = -pointers.get(root1);
        // Get the size of the second collection
        int size2 = -pointers.get(root2);
        // Calculate the combined total size
        int totalSize = size1 + size2;

        // Define the new root node and the merged child node
        int newroot;
        int childnode;
        // If the size of the first collection is smaller
        // than that of the second collection
        if (size1 < size2) {
            // the root node of the second collection is the new root node
            // and the root node of the first collection is the child node
            newroot = root2;
            childnode = root1;
            // Otherwise, the root node of the first collection is the new root node
            // and the root node of the second collection is the child node
        } else {
            newroot = root1;
            childnode = root2;
        }
        // Update the size of the new root node
        pointers.set(newroot, -totalSize);
        // Points the parent of the child node to the new root node
        pointers.set(childnode, newroot);

        return true;
    }
}
