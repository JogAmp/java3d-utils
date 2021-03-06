/*
 * Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 */

package org.jogamp.java3d.utils.scenegraph.io.state.org.jogamp.java3d.utils.geometry;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.jogamp.java3d.SceneGraphObject;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Color3f;

import org.jogamp.java3d.utils.geometry.Text2D;
import org.jogamp.java3d.utils.scenegraph.io.retained.Controller;
import org.jogamp.java3d.utils.scenegraph.io.retained.SymbolTableData;
import org.jogamp.java3d.utils.scenegraph.io.state.org.jogamp.java3d.LeafState;

public class Text2DState extends LeafState {

    // Text2D is really a subclass of Shape3D, however we don't want
    // the Shape3DState class to save the geometry or appearance data
    // so this class is a subclass of LeafState and we handle
    // the CollisionBounds in the read/write Object methods.

    private String text;
    private Color3f color;
    private String fontName;
    private int fontSize;
    private int fontStyle;

    public Text2DState(SymbolTableData symbol,Controller control) {
	super( symbol, control );

        if (node!=null) {
            Text2D t = (Text2D)node;
            text = t.getString();
            color = t.getColor();
            fontName = t.getFontName();
            fontSize = t.getFontSize();
            fontStyle = t.getFontStyle();
        }
    }

    @Override
    public void writeObject( DataOutput out ) throws IOException {
	super.writeObject( out );
        control.writeBounds( out, ((Shape3D)node).getCollisionBounds() );
    }

    @Override
    public void readObject( DataInput in ) throws IOException {
       super.readObject(in);
        ((Shape3D)node).setCollisionBounds( control.readBounds( in ));
    }

    @Override
    public void writeConstructorParams( DataOutput out ) throws IOException {
	super.writeConstructorParams( out );

        out.writeUTF( text );
        control.writeColor3f( out, color );
        out.writeUTF( fontName );
        out.writeInt( fontSize );
        out.writeInt( fontStyle );
    }

    @Override
    public void readConstructorParams( DataInput in ) throws IOException {
       super.readConstructorParams(in);

       text = in.readUTF();
       color = control.readColor3f( in );
       fontName = in.readUTF();
       fontSize = in.readInt();
       fontStyle = in.readInt();
    }

    @Override
    public SceneGraphObject createNode( Class j3dClass ) {
        // Create the node with a null appearance, we will add the appearance
        // during build graph
        Text2D text2D = (Text2D)createNode( j3dClass, new Class[] {
                                                String.class,
                                                Color3f.class,
                                                String.class,
                                                Integer.TYPE,
                                                Integer.TYPE },
                                        new Object[] {
                                                text,
                                                color,
                                                fontName,
                                                new Integer( fontSize ),
                                                new Integer( fontStyle )
                                                } );

        return text2D;
    }

    @Override
    protected org.jogamp.java3d.SceneGraphObject createNode() {
        return new Text2D( text, color, fontName, fontSize, fontStyle );
    }

}
