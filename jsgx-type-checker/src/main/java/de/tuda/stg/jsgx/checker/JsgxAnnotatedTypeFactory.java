package de.tuda.stg.jsgx.checker;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;

public class JsgxAnnotatedTypeFactory extends BaseAnnotatedTypeFactory {

    public JsgxAnnotatedTypeFactory(BaseTypeChecker checker)  {
        /*
        	Set useFlow to false if the flow analysis should be used.
         */
        super(checker, true);
        this.postInit();
    }
}
