package org.teree.shared.data;

import java.net.URL;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class NodeContent implements Cloneable {
    
    private Object value;
    
    public NodeContent clone() {
        NodeContent nc = new NodeContent();
        nc.setValue(getValue());
        return nc;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public Type getType() {
        return Type.valueOf(getValue().getClass());
    }
    
    public enum Type {
        None(null),
        String(String.class),
        IconString(IconString.class),
        URL(URL.class);
        
        private Class c;
        
        Type(Class c) {
            this.c = c;
        }
        
        public Class toClass() {
            return c;
        }
        
        public static Type valueOf(Class c) {
            Type[] v = values();
            for(int i=0; i<v.length; ++i){
                Type t = v[i];
                if(t.toClass() == c){
                    return t;
                }
            }
            return None;
        }
        
    }
    
}
