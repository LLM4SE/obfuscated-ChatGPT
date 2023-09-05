package nl.tudelft.obscure_openai.transformation;

import nl.tudelft.obscure_openai.Context;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtComment;

/**
 * Removes all comments from the code.
 */
public class RemoveComments extends AbstractProcessor<CtComment> {

  private Context context;

  public RemoveComments(Context context) {
    super();
    this.context = context;
  }

  @Override
  public void process(CtComment element) {
    element.delete();
  }
}
