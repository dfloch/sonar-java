/*
 * SonarQube Java
 * Copyright (C) 2012 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.java.checks;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

@Rule(
  key = "UnusedPrivateMethod",
  name = "Unused private method should be removed",
  tags = {"unused"},
  priority = Priority.MAJOR)
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("5min")
public class UnusedPrivateMethodCheck extends SubscriptionBaseVisitor {

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.METHOD, Tree.Kind.CONSTRUCTOR);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodTree node = (MethodTree) tree;
    Symbol symbol = node.symbol();
    if (symbol.isPrivate() && symbol.usages().isEmpty()) {
      if (node.is(Tree.Kind.CONSTRUCTOR)) {
        addIssue(node, "Remove this unused private \"" + node.simpleName().name() + "\" constructor.");
      } else if (!SerializableContract.SERIALIZABLE_CONTRACT_METHODS.contains(symbol.name()) && !hasExcludedAnnotation(node)) {
        addIssue(node, "Remove this unused private \"" + symbol.name() + "\" method.");
      }
    }
  }

  private boolean hasExcludedAnnotation(MethodTree method) {
    for (AnnotationTree annotation : method.modifiers().annotations()) {
      Type annotationType = annotation.symbolType();
      if (annotationType == null) {
        return false;
      }
      if (annotationType.isUnknown() || annotationType.isArray()) {
        return false;
      }
      if (annotationType.is("javax.annotation.PostConstruct")
        || annotationType.is("javax.annotation.PreDestroy")
        || annotationType.is("javax.enterprise.inject.Produces")
        || annotationType.is("javax.persistence.PostLoad")
        || annotationType.is("javax.persistence.PrePersist")
        || annotationType.is("javax.persistence.PostPersist")
        || annotationType.is("javax.persistence.PreUpdate")
        || annotationType.is("javax.persistence.PostUpdate")
        || annotationType.is("javax.persistence.PreRemove")
        || annotationType.is("javax.persistence.PostRemove")
        || annotationType.is("javax.ejb.Remove")
        || annotationType.is("javafx.fxml.FXML")) {
        return true;
      }

    }
    return false;
  }
}
