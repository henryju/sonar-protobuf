package org.sonar.protobuf.tree.impl.expression;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.expression.PrimitiveTypeTree;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.protobuf.tree.impl.ProtoBufTree;
import org.sonar.protobuf.tree.impl.lexical.InternalSyntaxToken;

public class PrimitiveTypeTreeImpl extends ProtoBufTree implements PrimitiveTypeTree {

  private final InternalSyntaxToken token;

  public PrimitiveTypeTreeImpl(InternalSyntaxToken token) {
    this.token = token;
  }

  @Override
  public Kind getKind() {
    return Kind.PRIMITIVE_TYPE;
  }

  @Override
  public void accept(VisitorCheck visitor) {
    visitor.visitPrimitiveType(this);
  }

  @Override
  public SyntaxToken keyword() {
    return token;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.<Tree>singletonIterator(token);
  }

}
