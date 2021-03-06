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
package org.sonar.java.checks.targets;

import java.util.stream.IntStream;
import javafx.fxml.FXML;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Remove;
import javax.enterprise.inject.Produces;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class UnusedPrivateMethod {

  public UnusedPrivateMethod(String s) {
    init();
  }

  private void init() {
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    // this method should not be considered as dead code, see Serializable contract
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    // this method should not be considered as dead code, see Serializable contract
  }

  private Object writeReplace() throws java.io.ObjectStreamException {
    // this method should not be considered as dead code, see Serializable contract
    return null;
  }

  private Object readResolve() throws java.io.ObjectStreamException {
    // this method should not be considered as dead code, see Serializable contract
    return null;
  }

  private void readObjectNoData() throws java.io.ObjectStreamException {
    // this method should not be considered as dead code, see Serializable contract
  }

  @SuppressWarnings("unused")
  private int unusedPrivateMethod() { // Noncompliant {{Remove this unused private "unusedPrivateMethod" method.}}
    return 1;
  }

  private int unusedPrivateMethod(int a, String s) { // Noncompliant {{Remove this unused private "unusedPrivateMethod" method.}}
    return 1;
  }

  public enum Attribute {
    ID("plop", "foo", true);

    Attribute(String prettyName, String type, boolean hidden) {}

    private Attribute(String name) {} // Noncompliant

    Attribute(String prettyName, String[][] martrix, int i) { // Noncompliant {{Remove this unused private "Attribute" constructor.}}
    }

  }

  private class A {
    A(int a) {}

    private A() {} // Noncompliant {{Remove this unused private "A" constructor.}}

    private <T> T foo(T t) {
      return null;
    }

    public void bar() {
      foo("");
    }
  }

  @PostConstruct
  private void unusedPrivateMethodPostConstruct() {}

  @PreDestroy
  private void unusedPrivateMethodPreDestroy() {}

  @Produces
  private void unusedPrivateMethodProduces() {}

  @PostLoad
  private void unusedPrivateMethodPostLoad() {}

  @PrePersist
  private void unusedPrivateMethodPrePersist() {}

  @PrePersist
  private void unusedPrivateMethodPrePersist() {}

  @PostPersist
  private void unusedPrivateMethodPostPersist() {}

  @PreUpdate
  private void unusedPrivateMethodPreUpdate() {}

  @PostUpdate
  private void unusedPrivateMethodPostUpdate() {}

  @PreRemove
  private void unusedPrivateMethodPreRemove() {}

  @PostRemove
  private void unusedPrivateMethodPostRemove() {}

  @Remove
  private void unusedPrivateMethodRemove() {}

  @FXML
  private void unusedPrivateMethodFXML() {}

}

class Lambdas {
  void method(){
    IntStream.range(1, 5)
      .map((x)-> x*x )
      .map(x -> x * x)
      .map((int x) -> x * x)
      .map((x)-> x*x )
    ;
  }
}
