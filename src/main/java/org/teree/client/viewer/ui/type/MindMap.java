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

public class MindMap implements MapType {
    
    //private static final int MARGIN
    
    private int max_width;
    private int lefth; // left height
    private int righth; // right height
    
    @Override
    public void resize(AbsolutePanel panel) {
        Iterator<Widget> it = panel.iterator();
        while(it.hasNext()){
            Widget w = it.next();
            if(w instanceof NodeWidget){
                NodeWidget nw = (NodeWidget) w;
                NodeContent nc = nw.getNode().getContent();
                nc.setWidth(nw.getContent().getWidgetWidth());
                nc.setHeight(nw.getContent().getWidgetHeight());
            }
        }
    }
    
    @Override
    public void prepare(AbsolutePanel panel, Node root) {
        panel.add(new NodeWidget(root), 0, 0);
        List<Node> cn = root.getChildNodes();
        for(int i=0; cn != null && i<cn.size(); ++i){
            prepare(panel, cn.get(i));
        }
    }
    
    @Override
    public void generate(AbsolutePanel panel, Node root) {

        panel.clear();
        Canvas canvas = Canvas.createIfSupported();
        panel.add(canvas);

        /**
         * left.get(col).get(row)
         */
        List<List<Integer>> left = new ArrayList<List<Integer>>();
        left.add(new ArrayList());
        List<List<Integer>> right = new ArrayList<List<Integer>>();
        right.add(new ArrayList());

        int maxlw = 0; // max left width
        int maxrw = 0; // max right width
        List<Node> rootcn = root.getChildNodes();
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
        
        List<Node> lcn = new ArrayList<Node>();
        List<Node> rcn = new ArrayList<Node>();
        lefth = 0;
        righth = 0;
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
        //panel.setWidth(max_x + "px");
        //panel.setHeight(max_y + "px");
        panel.setWidth("100%");
        panel.setHeight("100%");
        canvas.setCoordinateSpaceWidth(max_x);
        canvas.setCoordinateSpaceHeight(max_y);

        Context2d context = canvas.getContext2d();
        System.out.println("max_x:"+max_x + " max_y:" + max_y);

        context.beginPath();
        context.setLineWidth(1.0);
        panel.add(new NodeWidget(root), max_x/2 - root.getContent().getWidth(), max_y/2 - root.getContent().getHeight()); // add root node into middle of scene
        context.setFillStyle("red");
        context.fillRect(0, 0, max_x, max_y);
        drawLine(context, max_x/2 - root.getContent().getWidth(), max_y/2, max_x/2, max_y/2);
        
        int lh = 0, rh = 0;
        lh = (lefth < righth)?(righth-lefth)/2:0;
        rh = (righth < lefth)?(lefth-righth)/2:0;

        generate(panel, context, lcn, left, NodeLocation.LEFT, max_x/2 - root.getContent().getWidth(), max_y/2 - root.getContent().getHeight(), 0, lh);
        generate(panel, context, rcn, right, NodeLocation.RIGHT, max_x/2, max_y/2 - root.getContent().getHeight(), 0, rh);

        context.stroke();
    }
    
    /**
     * 
     * @param panel
     * @param canvas
     * @param cn
     * @param level_bounds
     * @param loc
     * @param start_x the point where arrow begins
     * @param start_y the point where arrow begins
     * @param level
     * @param lasth
     */
    private void generate(AbsolutePanel panel, Context2d context, List<Node> cn, List<List<Integer>> level_bounds, NodeLocation loc, int start_x, int start_y, int level, int lasth) {
        /**
        while(!cn.isEmpty()){
            List<Node> current = cn.get(i);
            for(int k=0; k<current.size(); ++k){
                Node n = current.get(k);
                cn.add(n.getChildNodes());
                // render n
                y += level_bounds.get(level).get(k);
                x = last_x.get(i);
                int lx = (loc == NodeLocation.LEFT)?x-n.getContent().getWidth():x;
                panel.add(new NodeWidget(n), lx, y);
                if(loc == NodeLocation.RIGHT)
                    lx += n.getContent().getWidth();
                last_x.add(lx);
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
        }*/
        //int h = getLevelHeight(level_bounds.get(level));
        int py = lasth;
        int lvl = 0;
        for(int i=0; i<cn.size(); ++i){
            Node n = cn.get(i);
            int x = (loc == NodeLocation.LEFT)?start_x-n.getContent().getWidth():start_x;
            lvl = ((level_bounds.size() > level)?level_bounds.get(level).get(i)/2:0);
            int y = lvl + py;
            
            System.out.println("node:" +n.getContent().getText()+"y:" + y);
            System.out.println(" lvl:"+((level_bounds.size() > level)?level_bounds.get(level).get(i)/2:0));
            panel.add(new NodeWidget(n), x, y-n.getContent().getHeight()/2);
            drawLine(context, start_x, start_y, x+((loc == NodeLocation.LEFT)?n.getContent().getWidth():0), y);
            
            if(n.getChildNodes() != null && n.getChildNodes().size() > 0){ // generate child nodes
                x = (loc == NodeLocation.RIGHT)?start_x+n.getContent().getWidth():x;
                //y += n.getContent().getHeight();
                generate(panel, context, n.getChildNodes(), level_bounds, loc, x, y, level+1, py);
            }
            py += lvl*2; // !!!
        }
    }
    
    private int setBounds(List<Node> cn, List<List<Integer>> level_bounds, int current_width, int level) {
        int bounds = 0;
        for(int i=0; i<cn.size(); ++i){
            Node n = cn.get(i);
            List<Node> fcn = n.getChildNodes();
            if (fcn != null && !fcn.isEmpty()) {
                int h = setBounds(fcn, level_bounds, current_width+n.getContent().getWidth(), level+1);
                bounds += h;
                level_bounds.get(level).add((n.getContent().getHeight()>h)?n.getContent().getHeight():h); // add max height of the node and its child nodes
            } else { // leaf
                bounds += n.getContent().getHeight();
                while (level_bounds.size() <= level) {
                    level_bounds.add(new ArrayList<Integer>());
                }
                level_bounds.get(level).add(n.getContent().getHeight()); // add height of the node which is leaf
                
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
