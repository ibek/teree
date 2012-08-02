package org.teree.client.viewer.ui.type;

import java.util.ArrayList;
import java.util.List;

import org.teree.client.viewer.ui.widget.NodeWidget;
import org.teree.client.viewer.ui.widget.event.Regenerate;
import org.teree.client.viewer.ui.widget.event.SelectNode;
import org.teree.shared.data.Node;
import org.teree.shared.data.Node.NodeLocation;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class MindMap extends MapType {
    
    private static final int MARGIN = 25;
    private static final int CURVENESS = 20;
    
    private int max_width;
    
    @Override
    public void generate(AbsolutePanel panel, Node root, Regenerate reg, boolean editable) {

        panel.clear();
        Canvas canvas = Canvas.createIfSupported();
        
        canvas.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SelectNode.unselect();
            }
        });
        
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
        for(int i=0; rootcn != null && i<rootcn.size(); ++i){
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
                if (maxlw < n.getContent().getWidth()) {
                    maxlw = n.getContent().getWidth();
                }
            } else {
                right.get(0).add(n.getContent().getHeight());
                if (maxrw < n.getContent().getWidth()) {
                    maxrw = n.getContent().getWidth();
                }
            }
        }
        
        List<Node> lcn = new ArrayList<Node>(); // left child nodes
        List<Node> rcn = new ArrayList<Node>(); // right child nodes
        int lefth = 0; // left height
        int righth = 0; // right height
        
        // split child nodes to left and right
        for(int i=0; rootcn != null && i<rootcn.size(); ++i){
            Node n = rootcn.get(i);
            if (n.getLocation() == NodeLocation.LEFT) {
                lefth += left.get(0).get(lcn.size());
                lcn.add(n);
            } else {
                righth += right.get(0).get(rcn.size());
                rcn.add(n);
            }
        }

        maxlw += left.size()*MARGIN;
        maxrw += right.size()*MARGIN;
        int max_x = maxlw + root.getContent().getWidth() + maxrw;
        int max_y = (lefth > righth)?lefth:righth;
        max_y += root.getContent().getHeight()/2;
        max_y = (max_y/2 - root.getContent().getHeight() > 0)?max_y:root.getContent().getHeight()*2; // for case root is biggest
        canvas.setCoordinateSpaceWidth(max_x);
        canvas.setCoordinateSpaceHeight(max_y);
        Context2d context = canvas.getContext2d();

        context.beginPath(); // of lines
        NodeWidget nw = new NodeWidget(root, reg, editable);
        panel.add(nw, maxlw, max_y/2 - root.getContent().getHeight()); // add root node into middle of scene
        
        // support content
        context.setFillStyle("white");
        context.fillRect(0, 0, max_x, max_y);
        
        // underline root
        drawLine(context, maxlw, max_y/2, maxlw+root.getContent().getWidth(), max_y/2);
        
        int lh = 0, rh = 0;

        // start position for child nodes where root height is biggest
        // and for child nodes where height is smaller
        lh += (max_y-lefth)/2;
        rh += (max_y-righth)/2;
        
        generate(panel, context, lcn, left, NodeLocation.LEFT, maxlw, max_y/2, 0, lh, reg, editable);
        generate(panel, context, rcn, right, NodeLocation.RIGHT, maxlw+root.getContent().getWidth(), max_y/2, 0, rh, reg, editable);

        context.stroke(); // draw the lines
    }
    
    @Override
    public NodeLocation getRootChildNodeLocation(Node root) {
        int left = 0, right = 0;
        List<Node> cn = root.getChildNodes();
        if(cn == null || cn.isEmpty()){
            return NodeLocation.RIGHT;
        }
        for(int i=0; i<cn.size(); ++i){
            Node n = cn.get(i);
            if(n.getLocation() == NodeLocation.LEFT){
                left++;
            }else if(n.getLocation() == NodeLocation.RIGHT){
                right++;
            }
        }
        return (left < right)?NodeLocation.LEFT:NodeLocation.RIGHT;
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
            NodeLocation loc, int start_x, int start_y, int level, int start_cn, Regenerate reg, boolean editable) {

        int py = start_cn;
        int lvl = 0;
        
        int margin = (loc == NodeLocation.LEFT)?-MARGIN:MARGIN;
        int curveness = (loc == NodeLocation.LEFT)?-CURVENESS:CURVENESS;
        
        for(int i=0; i<cn.size(); ++i){
            Node n = cn.get(i);
            int x = (loc == NodeLocation.LEFT)?start_x-n.getContent().getWidth():start_x;
            x += margin;
            lvl = ((level_bounds.size() > level)?level_bounds.get(level).get(i)/2:0);
            int y = lvl + py;

            NodeWidget nw = new NodeWidget(n, reg, editable);
            panel.add(nw, x, y-n.getContent().getHeight()/2);
            // underline node
            drawLine(context, x, y+n.getContent().getHeight()/2, x+n.getContent().getWidth(), y+n.getContent().getHeight()/2);
            // draw arrow
            drawCurve(context, start_x, start_y, x+((loc == NodeLocation.LEFT)?n.getContent().getWidth():0), y+n.getContent().getHeight()/2, curveness);
            //drawLine(context, start_x, start_y, x+((loc == NodeLocation.LEFT)?n.getContent().getWidth():0), y+n.getContent().getHeight()/2);
            
            if(n.getChildNodes() != null && n.getChildNodes().size() > 0){ // generate child nodes
                x = (loc == NodeLocation.RIGHT)?start_x+n.getContent().getWidth():x;
                //y += n.getContent().getHeight();
                generate(panel, context, n.getChildNodes(), level_bounds, loc, x+((loc == NodeLocation.LEFT)?0:margin),
                		y+n.getContent().getHeight()/2, level+1, py, reg, editable);
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
    
    private void drawCurve(Context2d context, int x1, int y1, int x2, int y2, int curveness) {
        context.moveTo(x1, y1);
        context.bezierCurveTo(x1 + curveness, y1, x2 - curveness, y2, x2, y2);
    }
    
    private void drawLine(Context2d context, int x1, int y1, int x2, int y2) {
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
    }

}
