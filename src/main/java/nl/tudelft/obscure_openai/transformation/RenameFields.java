package nl.tudelft.obscure_openai.transformation;

import nl.tudelft.obscure_openai.Context;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.reference.CtReference;
import spoon.reflect.visitor.chain.CtConsumer;
import spoon.reflect.visitor.filter.FieldReferenceFunction;
/**
 * Rename fields name.
 */
public class RenameFields extends AbstractProcessor<CtField<?>> {

  private Context context;

  public RenameFields(Context context) {
    super();
    this.context = context;
  }

  @Override
  public void process(CtField<?> element) {
    String newName = context.getRenameStrategy().rename(element.getSimpleName());
    element.map(new FieldReferenceFunction()).forEach(new CtConsumer<CtReference>() {
      @Override
      public void accept(CtReference t) {
        t.setSimpleName(newName);
      }
    });
    element.setSimpleName(newName);
  }
}
