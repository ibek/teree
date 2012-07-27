package org.teree.client.viewer.ui.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.teree.client.viewer.ui.Box;
import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class MindMap implements MapType {
    
    //private static final int MARGIN
    
    private int max_width;
    private Node _root;
    
    @Override
    public void generate(AbsolutePanel panel, Node root) {
        _root = root;

        panel.clear();
        Canvas canvas = Canvas.createIfSupported();
        panel.add(canvas);
        Context2d context = canvas.getContext2d();

        /**
         * left.get(col).get(row)
         */
        List<List<Integer>> left = new ArrayList<List<Integer>>();
        List<List<Integer>> right = new ArrayList<List<Integer>>();
        max_width = 0;
        setBounds(root, left, NodeLocation.LEFT);
        int maxlw = max_width;
        max_width = 0;
        setBounds(root, right, NodeLocation.RIGHT);
        int maxrw = max_width;
        
        List<List<Node>> cn = new ArrayList<List<Node>>();
        //cn.add(root.getChildNodes());
        List<Node> lcn = new ArrayList<Node>();
        List<Node> rcn = new ArrayList<Node>();
        List<Node> rootcn = root.getChildNodes();
        
        int lefth = 0;
        int righth = 0;
        
        for(int i=0; i<rootcn.size(); ++i){
            Node n = rootcn.get(i);
            if (n.getLocation() == NodeLocation.LEFT) {
                lefth += left.get(0).get(lcn.size());
                lcn.add(n);
            } else {
                righth += right.get(0).get(rcn.size());
                rcn.add(n);
            }
        }

        int max_x = maxlw + root.getContent().getWidth() + maxrw;
        int max_y = (lefth > righth)?lefth:righth;
        panel.setWidth(max_x + "px");
        panel.setHeight(max_y + "px");
        
        panel.add(new NodeWidget(root), max_x/2 - root.getContent().getWidth()/2, max_y/2 - root.getContent().getHeight()/2);

        cn.add(lcn);
        generate(panel, canvas, cn, NodeLocation.LEFT, max_x/2 - root.getContent().getWidth()/2, max_y/2 - lefth/2);
        cn.add(rcn);
        generate(panel, canvas, cn, NodeLocation.RIGHT, max_x/2 + root.getContent().getWidth()/2, max_y/2 - righth/2);
        
    }
    
    private void generate(AbsolutePanel panel, Canvas canvas, List<List<Node>> cn, NodeLocation loc, int start_x, int start_y) {
        int i = 0, level = 0, mark = 0;
        int x = start_x;
        int y = start_y;
        List<Integer> last_x = new ArrayList<Integer>();
        while(!cn.isEmpty()){
            List<Node> current = cn.get(i);
            for(int k=0; k<current.size(); ++k){
                Node n = current.get(k);
                cn.add(n.getChildNodes());
                // render n
                y += n.getContent().getHeight();
                int lx = (loc == NodeLocation.LEFT)?x-n.getContent().getWidth():x
                last_x.add(x-n.getContent().getWidth());
                panel.add(new NodeWidget(n), , y);
            }
            i++;
            if (i >= cn.size()) {
                level++;
                i = 0;
                for(int k=mark; mark >= 0; --k){
                    cn.remove(k);
                }
                mark = cn.size()-1;
            }
        }
    }
    
    private void setBounds(Node node, List<List<Integer>> bounds, NodeLocation loc) {
        List<Node> cn = node.getChildNodes();
        for(int i=0; i<cn.size(); ++i){
            Node n = cn.get(i);
            if (n.getLocation() == loc) {
                setBounds(n.getChildNodes(), bounds, n.getContent().getWidth(), 0);
            }
        }
    }
    
    private List<Integer> setBounds(List<Node> cn, List<List<Integer>> level_bounds, int current_width, int level) {
        List<Integer> bounds = new ArrayList<Integer>();
        for(int i=0; i<cn.size(); ++i){
            Node n = cn.get(i);
            List<Node> fcn = n.getChildNodes();
            if (fcn != null && !fcn.isEmpty()) {
                List<Integer> h = setBounds(fcn, level_bounds, current_width+n.getContent().getWidth(), level+1);
                bounds.add(n.getContent().getHeight()+Collections.max(h));
                level_bounds.get(level).add(n.getContent().getHeight()+Collections.max(h));
            } else { // leaf
                bounds.add(n.getContent().getHeight());
                if (level_bounds.get(level) == null) {
                    level_bounds.add(new ArrayList<Integer>());
                }
                level_bounds.get(level).add(n.getContent().getHeight());
                
                if (max_width < current_width + n.getContent().getWidth()) {
                    max_width = current_width + n.getContent().getWidth();
                }
            }
        }
        return bounds;
    }

}
