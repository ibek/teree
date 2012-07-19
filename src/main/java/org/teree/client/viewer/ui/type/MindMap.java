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
    
    private int max_width;
    
    @Override
    public void generate(AbsolutePanel panel, Node root) {

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
        cn.add(root.getChildNodes());
        
        int i = 0, level = 0, mark = 0;
        while(!cn.isEmpty()){
            List<Node> current = cn.get(i);
            for(int k=0; k<current.size(); ++k){
                Node n = current.get(k);
                cn.add(n.getChildNodes());
                // render n
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
        
        int max_x = maxlw + root.getContent().getWidth() + maxrw;
        int max_y = 0;// TODO
        panel.setWidth(max_x + "px");
        panel.setHeight(max_y + "px");
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
