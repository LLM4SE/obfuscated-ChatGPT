package nl.tudelft.obscure_openai.rename_strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import nl.tudelft.obscure_openai.Context;
import spoon.reflect.declaration.CtNamedElement;
import spoon.reflect.visitor.filter.AbstractFilter;

public class PermuteStragegy implements RenameStrategy {

  private Context context;
  private Map<String, String> mapping;

  private Random generator;

  public PermuteStragegy(Context context) {
    super();
    this.context = context;
    this.generator = new Random(context.getRandomSeed());

    Set<String> names = new HashSet<String>();

    this.context.getModel().getElements(new AbstractFilter<CtNamedElement>() {
      @Override
      public boolean matches(CtNamedElement element) {
        return true;
      }
    }).forEach(element -> {
      if (element.getSimpleName() != null && element.getSimpleName().length() > 1) {
        names.add(element.getSimpleName());
      }
    });

    mapping = new java.util.HashMap<String, String>();
    List<String> sortedNames = new ArrayList<String>(names);
    Collections.sort(sortedNames, String.CASE_INSENSITIVE_ORDER);

    List<String> randomizeNames = new ArrayList<String>(names);
    randomizeNames.sort(new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return generator.nextDouble() > 0.5 ? 1 : -1;
      }
    });

    for (int i = 0; i < sortedNames.size(); i++) {
      mapping.put(sortedNames.get(i), randomizeNames.get(i));
    }
  }

  @Override
  public String rename(String name) {
    return mapping.getOrDefault(name, name);
  }

}
