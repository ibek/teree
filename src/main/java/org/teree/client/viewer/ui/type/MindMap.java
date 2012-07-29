package org.teree.client.viewer.ui.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.teree.client.viewer.ui.Box;
import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;
import org.teree.shared.data.NodeContent;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class MindMap extends MapType {
    
    //private static final int MARGIN
    
    private int max_width;
    
    @Override
    public void generate(AbsolutePanel panel, Node root) {

        panel.clear();
        Canvas canvas = Canvas.createIfSupported();
        panel.add(canvas);

        /**
         * left.get(col).get(row)
         */
        List<List<Integer>> left = new ArrayList<List<Integer>>(); // left bounds
        left.add(new ArrayList<Integer>());
        List<List<Integer>> right = new ArrayList<List<Integer>>(); // right bounds
        right.add(new ArrayList<Integer>());

        int maxlw = 0; // max left width
        int maxrw = 0; // max right width
        List<Node> rootcn = root.getChildNodes();
        
        // set bounds for left and right nodes
        for(int i=0; i<rootcn.size(); ++i){
            Node n = rootcn.get(i);
            if (n.getLocation() == NodeLocation.LEFT && n.getChildNodes() != null) {
                max_width = 0;
                int h = setBounds(n.getChildNodes(), left, n.getContent().getWidth(), 1);
                left.get(0).add(h);
                if (max_width > maxlw) {
                    maxlw = max_width;
                }
            } else if (n.getChildNodes() != null) {
                max_width = 0;
                int h = setBounds(n.getChildNodes(), right, n.getContent().getWidth(), 1);
                right.get(0).add(h);
                if (max_width > maxrw) {
                    maxrw = max_width;
                }
            } else if(n.getLocation() == NodeLocation.LEFT) {
                left.get(0).add(n.getContent().getHeight());
            } else {
                right.get(0).add(n.getContent().getHeight());
            }
            
            if (maxlw < n.getContent().getWidth()) {
                maxlw = n.getContent().getWidth();
            }
            if (maxrw < n.getContent().getWidth()) {
                maxrw = n.getContent().getWidth();
            }
        }
        
        List<Node> lcn = new ArrayList<Node>(); // left child nodes
        List<Node> rcn = new ArrayList<Node>(); // right child nodes
        int lefth = 0; // left height
        int righth = 0; // right height
        
        // split child nodes to left and right
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
        max_y += root.getContent().getHeight()/2;
        max_y = (max_y/2 - root.getContent().getHeight() > 0)?max_y:root.getContent().getHeight()*2; // for case root is biggest
        canvas.setCoordinateSpaceWidth(max_x);
        canvas.setCoordinateSpaceHeight(max_y);
        Context2d context = canvas.getContext2d();

        context.beginPath(); // of lines
        panel.add(new NodeWidget(root), maxlw, max_y/2 - root.getContent().getHeight()); // add root node into middle of scene
        
        // support content
        context.setFillStyle("red");
        context.fillRect(0, 0, max_x, max_y);
        
        // underline root
        drawLine(context, maxlw, max_y/2, maxlw+root.getContent().getWidth(), max_y/2);
        
        // start position for child nodes where height is smaller
        int lh = 0, rh = 0;
        lh = (lefth < righth)?(righth-lefth)/2:0;
        rh = (righth < lefth)?(lefth-righth)/2:0;

        generate(panel, context, lcn, left, NodeLocation.LEFT, maxlw, max_y/2 - root.getContent().getHeight(), 0, lh);
        generate(panel, context, rcn, right, NodeLocation.RIGHT, maxlw+root.getContent().getWidth(), max_y/2 - root.getContent().getHeight(), 0, rh);

        context.stroke(); // draw the lines
    }
    
    /**
     * Generate part of mind map.
     * @param panel
     * @param canvas
     * @param cn child nodes
     * @param level_bounds
     * @param loc node location
     * @param start_x the point where arrow begins
     * @param start_y the point where arrow begins
     * @param level depth
     * @param start_cn the point where child nodes begins (Y)
     */
    private void generate(AbsolutePanel panel, Context2d context, List<Node> cn, List<List<Integer>> level_bounds, 
            NodeLocation loc, int start_x, int start_y, int level, int start_cn) {

        int py = start_cn;
        int lvl = 0;
        for(int i=0; i<cn.size(); ++i){
            Node n = cn.get(i);
            int x = (loc == NodeLocation.LEFT)?start_x-n.getContent().getWidth():start_x;
            lvl = ((level_bounds.size() > level)?level_bounds.get(level).get(i)/2:0);
            int y = lvl + py;
            
            panel.add(new NodeWidget(n), x, y-n.getContent().getHeight()/2);
            // underline node
            drawLine(context, x, y+n.getContent().getHeight()/2, x+n.getContent().getWidth(), y+n.getContent().getHeight()/2);
            // draw arrow
            drawLine(context, start_x, start_y, x+((loc == NodeLocation.LEFT)?n.getContent().getWidth():0), y+n.getContent().getHeight()/2);
            
            if(n.getChildNodes() != null && n.getChildNodes().size() > 0){ // generate child nodes
                x = (loc == NodeLocation.RIGHT)?start_x+n.getContent().getWidth():x;
                //y += n.getContent().getHeight();
                generate(panel, context, n.getChildNodes(), level_bounds, loc, x, y, level+1, py);
            }
            py += lvl*2; // for next row, increase py
        }
    }
    
    /**
     * Set required place for nodes in level_bounds.
     * @param cn child nodes
     * @param level_bounds
     * @param current_width
     * @param level
     * @return height of child nodes (cn)
     */
    private int setBounds(List<Node> cn, List<List<Integer>> level_bounds, int current_width, int level) {
        int bounds = 0;
        for(int i=0; i<cn.size(); ++i){
            Node n = cn.get(i);
            List<Node> fcn = n.getChildNodes();
            if (fcn != null && !fcn.isEmpty()) {
                int h = setBounds(fcn, level_bounds, current_width+n.getContent().getWidth(), level+1); // recursively get height
                bounds += h;
                level_bounds.get(level).add((n.getContent().getHeight()>h)?n.getContent().getHeight():h); // add max height of the node and its child nodes
            } else { // leaf
                bounds += n.getContent().getHeight();
                while (level_bounds.size() <= level) {
                    level_bounds.add(new ArrayList<Integer>());
                }
                level_bounds.get(level).add(n.getContent().getHeight()); // add height of the node which is leaf
                
                // !!! this part set maximal width
                if (max_width < current_width + n.getContent().getWidth()) {
                    max_width = current_width + n.getContent().getWidth();
                }
            }
        }
        return bounds;
    }
    
    private void drawLine(Context2d context, int x1, int y1, int x2, int y2) {
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
    }

}
