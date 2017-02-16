import javafx.scene.Group;
import javafx.scene.shape.Line;

/**
 * BNode class, the basic element which contains the data,
 * a pointer to the left node and a pointer to the right node.
 */

public class AVLNode {
		Integer item;
	    AVLNode left;
	    AVLNode right;
	    int depth;
	    int height;
	    int balanceFactor;

        int x;
        int y;
        Group ring;
        Line leftLine;
        Line rightLine;

        /**
         * BNode Constructor. Set data and pointers for a new BNode.
         *
         * @param item   the data stored in node
         * @param left   a pointer the left BNode
         * @param right  a pointer to the right BNode
         * @param parent a pointer to the parent BNode
         */
        AVLNode(Integer item) {
            this.item = item;
            depth = 0;
            height = 0;
            balanceFactor = 0;
            x = 50;
            y = 250;
        }

        
        public int getX() {
        	return x;
        }
        
        public int getY() {
        	return y;
        }
        
        public Group getRing() {
        	return ring;
        }
        
        public void setX(int x) {
        	this.x = x;
        }
        
        public void setY(int y) {
        	this.y = y;
        }
        
        public void setRing(Group ring) {
        	this.ring = ring;
        }

    }