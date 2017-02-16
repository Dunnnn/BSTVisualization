import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class VisualizationHelper {
	
	final Pane canvas;

	
	final Duration DURATION= Duration.millis(300);
	
	final int INRADIUS = 8;
	final int OUTRADIUS = 10;
	final Color RINGCOLOR = Color.BLUE;
	
	public VisualizationHelper(Pane canvas) {
		this.canvas = canvas;
	}

	public FadeTransition drawNode(AVLNode node) {
		Line leftLine = new Line();
		Line rightLine = new Line();
		leftLine.setStroke(Color.BLUE);
		leftLine.setOpacity(0);
		rightLine.setStroke(Color.BLUE);
		rightLine.setOpacity(0);
		node.leftLine = leftLine;
		node.rightLine = rightLine;
		
		Circle outCircle = new Circle(node.getX(), node.getY(), OUTRADIUS);
		Circle inCircle = new Circle(node.getX(), node.getY(), INRADIUS);
		String num = node.item.toString();
		Text number = new Text(node.getX() - 4 - (num.length() > 1?(num.length() * 2) : 0), node.getY() + 5, num);
		Text height = new Text(node.getX() - 17, node.getY() - 10, new Integer(node.height).toString());
		Text bf = new Text(node.getX() + 9, node.getY() - 10, new Integer(node.balanceFactor).toString());

		outCircle.setFill(Color.BLUE);
		inCircle.setFill(Color.WHITE);
		Group ring = new Group();
		ring.setOpacity(0);
		
		ring.getChildren().addAll(outCircle, inCircle, number, height, bf);
		node.setRing(ring);
		canvas.getChildren().addAll(leftLine, rightLine, ring);
		
		FadeTransition ft = new FadeTransition(DURATION, ring);
		ft.setFromValue(0);
		ft.setToValue(1);
		

		return ft;
	}
	
	public FadeTransition removeNode(AVLNode node) {
		Group ring = node.ring;
				
		FadeTransition ft = new FadeTransition(DURATION, ring);
		ft.setFromValue(1);
		ft.setToValue(0);		

		return ft;
	}
	
	public TranslateTransition moveNodeTo(AVLNode node, int x, int y) {
		Group ring = node.ring;
		TranslateTransition tt = new TranslateTransition(DURATION, ring);
	    tt.setByX(x - node.getX());
	    tt.setByY(y - node.getY());
	    node.setX(x);
	    node.setY(y);
	    return tt;
	}
	
	public TranslateTransition moveToNode(AVLNode dNode, AVLNode sNode) {
		Group sRing = sNode.ring;
		TranslateTransition tt = new TranslateTransition(DURATION, sRing);
		int finalX = dNode.getX();
		int finalY = dNode.getY();
	    tt.setByX(finalX - sNode.getX());
	    tt.setByY(finalY - sNode.getY());
	    sNode.setX(finalX);
	    sNode.setY(finalY);
	    sNode.depth = dNode.depth;
	  
	    return tt;
	}
	
	
	
	public TranslateTransition moveToLeftNode(AVLNode pNode, AVLNode cNode) {
		Group cRing = cNode.ring;
		TranslateTransition tt = new TranslateTransition(DURATION, cRing);
		int finalX = (int) (pNode.getX() - 2500 / Math.pow(pNode.depth + 3, 3));
		int finalY = pNode.getY() + 50;
	    tt.setByX(finalX - cNode.getX());
	    tt.setByY(finalY - cNode.getY());
	    cNode.setX(finalX);
	    cNode.setY(finalY);
	    cNode.depth = pNode.depth + 1;
	    
	    return tt;
	}
	
	public TranslateTransition moveToRightNode(AVLNode pNode, AVLNode cNode) {
		Group cRing = cNode.ring;
		TranslateTransition tt = new TranslateTransition(DURATION, cRing);
		int finalX = (int) (pNode.getX() + 2500 / Math.pow(pNode.depth + 3, 3));
		int finalY = pNode.getY() + 50;
	    tt.setByX(finalX - cNode.getX());
	    tt.setByY(finalY - cNode.getY());
	    cNode.setX(finalX);
	    cNode.setY(finalY);
	    cNode.depth = pNode.depth + 1;
  
	    return tt;
	}
	
	public ParallelTransition moveToRightNodeWithChildren(AVLNode pNode, AVLNode cNode) {
		ParallelTransition pt = new ParallelTransition();
		pt.getChildren().add(moveToRightNode(pNode, cNode));
		if (cNode.left != null) {
			pt.getChildren().add(moveToLeftNodeWithChildren(cNode, cNode.left));
		}
		
		if (cNode.right != null) {
			pt.getChildren().add(moveToRightNodeWithChildren(cNode, cNode.right));
		}
		
		return pt;
		
	}
	
	public ParallelTransition moveToLeftNodeWithChildren(AVLNode pNode, AVLNode cNode) {
		ParallelTransition pt = new ParallelTransition();
		pt.getChildren().add(moveToLeftNode(pNode, cNode));
		if (cNode.left != null) {
			pt.getChildren().add(moveToLeftNodeWithChildren(cNode, cNode.left));
		}
		
		if (cNode.right != null) {
			pt.getChildren().add(moveToRightNodeWithChildren(cNode, cNode.right));
		}
		
		return pt;
	}
	
	public ParallelTransition moveToNodeWithChildren(AVLNode dNode, AVLNode sNode) {
		ParallelTransition pt = new ParallelTransition();
		pt.getChildren().add(moveToNode(dNode, sNode));
		if (sNode.left != null) {
			pt.getChildren().add(moveToLeftNodeWithChildren(dNode, sNode.left));
		}
		
		if (sNode.right != null) {
			pt.getChildren().add(moveToRightNodeWithChildren(dNode, sNode.right));
		}
		
		return pt;
	}
	
	public SequentialTransition glare(AVLNode node) {
		Group ring = node.ring;
		FillTransition ft = new FillTransition(DURATION, Color.BLUE, Color.RED);
		FillTransition ft2 = new FillTransition(DURATION, Color.RED, Color.BLUE);	
		ft.setShape((Circle) ring.getChildren().get(0));
		ft2.setShape((Circle) ring.getChildren().get(0));
		SequentialTransition st = new SequentialTransition();
		st.getChildren().add(ft);
		st.getChildren().add(ft2);
		
		return st;		
	}
	
	public ParallelTransition rotateRight(AVLNode node) {
		ParallelTransition pt = new ParallelTransition();
		pt.getChildren().add(moveToNode(node, node.left));
		pt.getChildren().add(moveToRightNode(node, node));
		if (node.right != null) {
			pt.getChildren().add(moveToRightNodeWithChildren(node, node.right));
		}
		if (node.left.right != null) {
			pt.getChildren().add(moveToLeftNodeWithChildren(node, node.left.right));
		}
		if (node.left.left != null) {
			pt.getChildren().add(moveToLeftNodeWithChildren(node.left, node.left.left));
		}
		return pt;
	}
	
	public ParallelTransition rotateLeft(AVLNode node) {
		ParallelTransition pt = new ParallelTransition();
		pt.getChildren().add(moveToNode(node, node.right));
		pt.getChildren().add(moveToLeftNode(node, node));
		if (node.left != null) {
			pt.getChildren().add(moveToLeftNodeWithChildren(node, node.left));
		}
		if (node.right.left != null) {
			pt.getChildren().add(moveToRightNodeWithChildren(node, node.right.left));
		}
		if (node.right.right != null) {
			pt.getChildren().add(moveToRightNodeWithChildren(node.right, node.right.right));
		}
		return pt;
	}
	
	public FadeTransition replaceItem(AVLNode rNode, AVLNode succNode) {
		Group rRing = rNode.ring;
		Text rNumber = (Text) rRing.getChildren().get(2);
		FadeTransition ft = new FadeTransition(DURATION, rNumber);
		ft.setFromValue(1);
		ft.setToValue(0);
		ft.setOnFinished(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	rNumber.setText(succNode.item.toString());
		    	rNumber.setOpacity(1);
		    }
		});
		
		return ft;
	}
	
	public ParallelTransition updateNode(AVLNode node) {
		Group ring = node.ring;
		Text height = (Text) ring.getChildren().get(3);
		Text bf = (Text) ring.getChildren().get(4);
		
		FadeTransition ft = new FadeTransition(DURATION, height);
		FadeTransition ft2 = new FadeTransition(DURATION, bf);
		ft.setFromValue(1);
		ft.setToValue(0);
		ft2.setFromValue(1);
		ft2.setToValue(0);
		
		ParallelTransition pt = new ParallelTransition();
		pt.getChildren().addAll(ft, ft2);	
		
		pt.setOnFinished(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	bf.setText(String.valueOf(node.balanceFactor));
		    	height.setText(String.valueOf(node.height));
		    	bf.setOpacity(1);
		    	height.setOpacity(1);
		    }
		});
		
		
		return pt;
	}
	

	public ParallelTransition clearLines(AVLNode node) {	
		ParallelTransition pt = new ParallelTransition();
		clearHelper(node, pt);
		return pt;

	}
	
	private void clearHelper(AVLNode node, ParallelTransition pt) {
		if (node != null) {
			Line leftLine = node.leftLine;
			Line rightLine = node.rightLine;
			
			if (leftLine.getOpacity() == 1) {
				FadeTransition ft = new FadeTransition(DURATION, leftLine);
				ft.setFromValue(1);
				ft.setToValue(0);
				pt.getChildren().add(ft);
			}
			
			if (rightLine.getOpacity() == 1) {
				FadeTransition ft = new FadeTransition(DURATION, rightLine);
				ft.setFromValue(1);
				ft.setToValue(0);
				pt.getChildren().add(ft);
			}
			
			if (node.left != null) {
				clearHelper(node.left, pt);
			}
			
			if (node.right != null) {
				clearHelper(node.right, pt);
			}
			
		}
		
	}
	
	public ParallelTransition redrawLines(AVLNode node) {	
		ParallelTransition pt = new ParallelTransition();
		redrawHelper(node, pt);
		return pt;

	}
	
	private void redrawHelper(AVLNode node, ParallelTransition pt) {
		if (node != null) {
			Line leftLine = node.leftLine;
			Line rightLine = node.rightLine;
			
			if (node.left != null) {

				FadeTransition ft = new FadeTransition(DURATION, leftLine);
				ft.setFromValue(0);
				ft.setToValue(1);
				pt.getChildren().add(ft);
				redrawHelper(node.left, pt);
			}
			
			if (node.right != null) {

				FadeTransition ft = new FadeTransition(DURATION, rightLine);
				ft.setFromValue(0);
				ft.setToValue(1);
				pt.getChildren().add(ft);
				redrawHelper(node.right, pt);
			}
		}
    }
	
	public void recalLines(AVLNode node) {
		if (node != null) {
			Line leftLine = node.leftLine;
			Line rightLine = node.rightLine;
			
			if (node.left != null) {
				leftLine.setStartX(node.x - 3);
				leftLine.setStartY(node.y + 3);
				leftLine.setEndX(node.left.x + 3);
				leftLine.setEndY(node.left.y - 3);
				recalLines(node.left);
			}
			
			if (node.right != null) {
				rightLine.setStartX(node.x + 3);
				rightLine.setStartY(node.y + 3);
				rightLine.setEndX(node.right.x - 3);
				rightLine.setEndY(node.right.y - 3);
				recalLines(node.right);
			}
		}
	}
		
	public void clean() {
		canvas.getChildren().clear();
	}
	

}
