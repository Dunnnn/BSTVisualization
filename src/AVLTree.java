import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by huangdun on 6/6/15.
 */
public class AVLTree {
    private AVLNode root;
    private int size;
    VisualizationHelper vlHelper;


    /**
     * BST Constructor. Initialize the size.
     */
    public AVLTree(VisualizationHelper vlHelper) {
        size = 0;
        this.vlHelper = vlHelper;
    }

    /**
     * Adds the item to the tree.  Duplicate items and null items should not be added. O(log n)
     *
     * @param item the item to add
     * @return true if item added, false if it was not
     */
    public boolean add(Integer item, Deque<Transition> animeDeque) {
    	
        //case1: when added item is empty
        if (item == null) {
            return false;
        }

        //case2: item is not empty, but BST is empty
        if ( isEmpty() ) {
            root = new AVLNode(item);
            animeDeque.addLast(vlHelper.drawNode(root));
			animeDeque.addLast(vlHelper.moveNodeTo(root, 245, 20));
            size++;
            return true;
        } else {
        	animeDeque.addLast(vlHelper.clearLines(root));
        	//case3: item is not empty and BST is also not empty
            boolean inserted = insert(item, root, animeDeque);

            if (inserted) {
                root = rebalance(root, animeDeque);
            }
            
            Transition transition = animeDeque.getLast();
            EventHandler<ActionEvent> preHandler = transition.getOnFinished();
			transition.setOnFinished(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			    	if (preHandler != null) {
			    		preHandler.handle(event);
			    	}
			    	
			    	vlHelper.recalLines(root);
			    }
			});

            animeDeque.addLast(vlHelper.redrawLines(root));
            return inserted;
        }
    }

    /**
     * Helper method which inserts the AVLNode which contains the item to the correct place in the
     * BST using recursion.
     *
     * @param item the item to add
     * @param node the node which contains data to be compared
     * @return true if item added, false if it was not
     */
    private boolean insert(Integer item, AVLNode node, Deque<Transition> animeDeque) {
    	animeDeque.addLast(vlHelper.glare(node));

        if (item.compareTo(node.item) < 0) {
            //case1: (recursion) item is smaller than the node and node still has left node
            if(node.left != null) {
                boolean inserted = insert(item, node.left, animeDeque);

                if(inserted) {
                    node.left = rebalance(node.left, animeDeque);
                }

                return inserted;
            } else {
                //case2: (base case) item is smaller than the node but the node doesn't have left node
                node.left = new AVLNode(item);
                animeDeque.addLast(vlHelper.drawNode(node.left));
    			animeDeque.addLast(vlHelper.moveToLeftNode(node, node.left));
                size++;
                return true;
            }

        } else if (item.compareTo(node.item) > 0) {
            //case1: (recursion) item is greater than the node and node still has right node
            if(node.right != null) {
                boolean inserted = insert(item, node.right, animeDeque);

                if(inserted) {
                    node.right = rebalance(node.right, animeDeque);
                }

                return inserted;
            } else {
                //case2: (base case) item is greater than the node but the node doesn't have right node
                node.right = new AVLNode(item);
                animeDeque.addLast(vlHelper.drawNode(node.right));
                animeDeque.addLast(vlHelper.moveToRightNode(node, node.right));
                size++;
                return true;
            }

        }

        //case5: (base case) item is found.
        return false;

    }

    private int height(AVLNode node) {
        return (node == null) ? -1:node.height;
    }

    private void updateAll(AVLNode node, Deque<Transition> animeDeque) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        node.balanceFactor = height(node.left) - height(node.right);
        animeDeque.addLast(vlHelper.updateNode(node));
        
    }

    private AVLNode rebalance(AVLNode node, Deque<Transition> animeDeque) {
        //case1: if node to be rebalanced is null
        if (node == null) {
            return null;
        }

        updateAll(node, animeDeque);

        if (node.balanceFactor > 1) {
            //case2: if node to be rebalanced is not null, it is unbalanced, its balance factor is
            // greater than 1, its left node's balance factor is greater than 0 (in fact, which could only be 1)
            // , then rotate left
            if (node.left.balanceFactor < 0) {
                node = rotateLeftRight(node, animeDeque);
            } else {
                //case3: if node to be rebalanced is not null, it is unbalanced, its balance factor is
                // greater than 1, its left node's balance factor is smaller than 0 (in fact, which could only be -1)
                // , then rotate left and then right
                node = rotateRight(node, animeDeque);
            }
        } else if (node.balanceFactor < -1) {
            //case3: if node to be rebalanced is not null, it is unbalanced, its balance factor is
            // smaller than -1, its left node's balance factor is smaller than 0 (in fact, which could only be -1)
            // , then rotate right
            if (node.right.balanceFactor > 0) {
                node = rotateRightLeft(node, animeDeque);
            } else {
                //case4: if node to be rebalanced is not null, it is unbalanced, its balance factor is
                // smaller than -1, its left node's balance factor is greater than 0 (in fact, which could only be 1)
                // , then rotate right and then left
                node = rotateLeft(node, animeDeque);
            }
        }

        //case5: node is balanced already
        return node;

    }

    private AVLNode rotateRight(AVLNode node, Deque<Transition> animeDeque) {
    	animeDeque.addLast(vlHelper.rotateRight(node));
        //when rotateRight is called, node.left cannot be null, si it is safe to do the following operations
        AVLNode newNode = node.left;
        node.left = newNode.right;
        newNode.right = node;
        updateAll(node, animeDeque);
        updateAll(newNode, animeDeque);
        return newNode;
    }

    private AVLNode rotateLeft(AVLNode node, Deque<Transition> animeDeque) {
    	animeDeque.addLast(vlHelper.rotateLeft(node));
        AVLNode newNode = node.right;
        node.right = newNode.left;
        newNode.left = node;
        updateAll(node, animeDeque);
        updateAll(newNode, animeDeque);
        return newNode;
    }
    private AVLNode rotateLeftRight(AVLNode node, Deque<Transition> animeDeque) {
        node.left = rotateLeft(node.left, animeDeque);
        return rotateRight(node, animeDeque);
    }

    private AVLNode rotateRightLeft(AVLNode node, Deque<Transition> animeDeque) {
        node.right = rotateRight(node.right, animeDeque);
        return rotateLeft(node, animeDeque);
    }


    /**
     * returns the maximum element held in the tree.  null if tree is empty. O(log n)
     * @return maximum item or null if empty
     */
    public Integer max() {
        if (isEmpty()) {
            return null;
        }

        AVLNode current = root;

        while (current != null ) {

            if (current.right == null) {
                return current.item;
            }

            current = current.right;
        }

        return null;
    }

    /**
     * returns the number of items in the tree
     * runs in O(1)
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * runs in O(1)
     *
     * @return true if tree has no elements, false if tree has anything in it.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * O(log n)
     * @return the minimum element in the tree or null if empty
     */
    public Integer min() {
        if (isEmpty()) {
            return null;
        }

        AVLNode current = root;

        while (current != null ) {

            if (current.left == null) {
                return current.item;
            }

            current = current.left;
        }

        return null;
    }

    /**
     * Checks for the given item in the tree.
     * runs in O(log n) expected, may be linear in worst case
     *
     * @param item the item to look for
     * @return true if item is in tree, false otherwise
     */
    public boolean contains(Integer item, Deque<Transition> animeDeque) {
        return find(item, root, animeDeque);
    }

    /**
     * Helper method which checks if the item is equal to
     * the data in the Node or otherwise calls itself to check its branch
     *
     * @param item the item to add
     * @param node the node which contains data to be compared
     * @return true if item added, false if it was not
     */
    private boolean find(Integer item, AVLNode node, Deque<Transition> animeDeque) {
        if (node == null) {
            return false;
        }

        animeDeque.addLast(vlHelper.glare(node));
        
        if (item.compareTo(node.item) < 0) {

            if(node.left == null) {
                return false;
            } else {
                return find(item, node.left, animeDeque);
            }

        } else if (item.compareTo(node.item) > 0) {

            if(node.right == null) {
                return false;
            } else {
                return find(item, node.right, animeDeque);
            }

        }

        return true;

    }

    /**
     * removes the given item from the tree O(log n)
     *
     * @param item the item to remove
     * @return true if item removed, false if item not found
     */
    public boolean remove(Integer item, Deque<Transition> animeDeque) {
        if (item==null) {
            return false;
        }

        if (isEmpty()) {
            return false;
        }

        int bfsize = size();
        animeDeque.addLast(vlHelper.clearLines(root));
        root = delete(item, root, animeDeque);
        
        Transition transition = animeDeque.getLast();
        EventHandler<ActionEvent> preHandler = transition.getOnFinished();
		transition.setOnFinished(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	if (preHandler != null) {
		    		preHandler.handle(event);
		    	}
		    	
		    	vlHelper.recalLines(root);
		    }
		});
        animeDeque.addLast(vlHelper.redrawLines(root));

        if (bfsize != size()) {
            return true;
        }

        return false;

    }

    /**
     * Helper method which checks if the item is equal to
     * the data. If it exists, delete the item, or calls itself to check its branch
     *
     * @param item the item to delete
     * @param node the node which contains data to be compared
     * @return true if item found and deleted, false if it was not
     */
    private AVLNode delete(Integer item, AVLNode node, Deque<Transition> animeDeque) {   	
        if(node == null) {
            return node;
        }
        
    	animeDeque.addLast(vlHelper.glare(node));

        if (item.compareTo(node.item) < 0) {
            node.left = delete(item, node.left, animeDeque);
        } else if (item.compareTo(node.item) > 0) {
            node.right = delete(item, node.right,  animeDeque);
        } else if (node.right != null && node.left != null) {
            AVLNode succ = findMin(node.right);
            node.item = succ.item;
            node.right = delete(node.item, node.right, animeDeque);
            animeDeque.addLast(vlHelper.replaceItem(node, succ));
        } else {
        	if (node.left == null && node.right == null) {
        		animeDeque.addLast(vlHelper.removeNode(node));
        	} else if (node.left == null) {
        		animeDeque.addLast(vlHelper.removeNode(node));
        		animeDeque.addLast(vlHelper.moveToNodeWithChildren(node, node.right));
        	} else {
        		animeDeque.addLast(vlHelper.removeNode(node));
        		animeDeque.addLast(vlHelper.moveToNodeWithChildren(node, node.left));
        	}
            node = (node.left != null) ? node.left : node.right;
            size--;
        }

        return rebalance(node, animeDeque);
    }

    /**
     * O(log n)
     * @return the minimum node after input node
     */
    public AVLNode findMin(AVLNode node) {
        if (node == null) {
            return null;
        }

        AVLNode current = node;

        while (current != null ) {

            if (current.left == null) {
                return current;
            }

            current = current.left;
        }

        return null;
    }

    /**
     * returns an iterator over this collection
     *
     * iterator is based on an in-order traversal
     */
    public Iterator<Integer> iterator() {
        return getInOrder().iterator();
    }

    /**
     * Runs in linear time
     * @return a list of the data in post-order traversal order
     */
    public List<Integer> getPostOrder() {
        ArrayList<Integer> postOrderList = new ArrayList<>();
        listPostOrder(root, postOrderList);
        return postOrderList;
    }

    /**
     * Helper recursive method which adds items to list in postorder
     * Base case is when node is null, do nothing
     *
     * @param node the current
     * @param list list which items need to be added to
     */
    private void listPostOrder(AVLNode node, ArrayList<Integer> list) {
        if (node != null) {
            listPostOrder(node.left, list);
            listPostOrder(node.right, list);
            list.add(node.item);
        }
    }

    /**
     * Runs in linear time
     * @return a list of the data in level-order traversal order
     */
    public List<Integer> getLevelOrder() {
        ArrayDeque<AVLNode> queue = new ArrayDeque<>();
        ArrayList<Integer> levelOrderList = new ArrayList<>();

        if(!isEmpty()) {
            queue.addLast(root);
        }

        while(!queue.isEmpty()) {
            AVLNode node = queue.pollFirst();
            if(node != null) {
                levelOrderList.add(node.item);

                if(node.left != null) {
                    queue.addLast(node.left);
                }

                if(node.right != null) {
                    queue.addLast(node.right);
                }
            }
        }

        return levelOrderList;
    }

    /**
     * Runs in linear time
     * @return a list of the data in pre-order traversal order
     */
    public List<Integer> getPreOrder() {
        ArrayList<Integer> preOrderList = new ArrayList<>();
        listPreOrder(root, preOrderList);
        return preOrderList;
    }

    /**
     * Helper recursive method which adds items to list in preorder
     * Base case is when node is null, do nothing
     *
     * @param node the current
     * @param list list which items need to be added to
     */
    private void listPreOrder(AVLNode node, ArrayList<Integer> list) {
        if (node != null) {
            list.add(node.item);
            listPreOrder(node.left, list);
            listPreOrder(node.right, list);
        }
    }

    /**
     * Runs in linear time
     * @return a list of the data in pre-order traversal order
     */
    public List<Integer> getInOrder() {
        ArrayList<Integer> inOrderList = new ArrayList<>();
        listInOrder(root, inOrderList);
        return inOrderList;
    }

    /**
     * Helper recursive method which adds items to list in inorder
     * Base case is when node is null, do nothing
     *
     * @param node the current
     * @param list list which items need to be added to
     */
    private void listInOrder(AVLNode node, ArrayList<Integer> list) {
        if (node != null) {
            listInOrder(node.left, list);
            list.add(node.item);
            listInOrder(node.right, list);
        }
    }

    /**
     * Runs in linear time
     * Removes all the elements from this tree
     */
    public void clear() {
        size = 0;
        root = null;
        vlHelper.clean();
    }

}
