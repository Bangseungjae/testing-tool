package com.bsj.dayonetest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.bsj.dayonetest.annotation.FastTest;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

class ArchitectureTest {

  JavaClasses javaClasses;

  @BeforeEach
  public void beforeEach() {
    javaClasses =
        new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("com.bsj.dayonetest");
  }

  @FastTest
  @DisplayName("controller 패키지 안에 있는 클래스들은 Api 끝나야 한다")
  void controllerTest() {
    ArchRule rule =
        classes()
            .that()
            .resideInAnyPackage("..controller")
            .should()
            .haveSimpleNameEndingWith("Api");

    ArchRule annotationRule =
        classes()
            .that()
            .resideInAnyPackage("..controller")
            .should()
            .beAnnotatedWith(RestController.class)
            .orShould()
            .beAnnotatedWith(Controller.class);
    rule.check(javaClasses);
    annotationRule.check(javaClasses);
  }

  @FastTest
  @DisplayName("request 패키지 안에 있는 클래스들은 Request로 끝나야 한다")
  void requestTest() {
    ArchRule rule =
        classes()
            .that()
            .resideInAnyPackage("..request..")
            .should()
            .haveSimpleNameEndingWith("Request");

    rule.check(javaClasses);
  }

  @FastTest
  @DisplayName("response 패키지 안에 있는 클래스는 Response로 끝나야 한다")
  void responseTest() {
    ArchRule rule =
        classes()
            .that()
            .resideInAnyPackage("..response..")
            .should()
            .haveSimpleNameEndingWith("Response");
  }

  @FastTest
  @DisplayName("repository 패키지 안에 있는 클래스는, 인터페이스여야 한다")
  void repositoryTest() {
    ArchRule rule =
        classes()
            .that()
            .resideInAnyPackage("..repository..")
            .should()
            .haveSimpleNameEndingWith("Repository")
            .andShould()
            .beInterfaces();
    rule.check(javaClasses);
  }

  @FastTest
  @DisplayName("service 패키지 안에 있는 클래스는 Service로 끝나야 하고, @Service 에너테이션이 붙어있어야 한다")
  void serviceTest() {
    ArchRule rule =
        classes()
            .that()
            .resideInAnyPackage("..service..")
            .should()
            .haveSimpleNameEndingWith("Service")
            .andShould()
            .beAnnotatedWith(Service.class);
    rule.check(javaClasses);
  }

  @FastTest
  @DisplayName("config 패키지 안에 있는 클래스는 Config으로 끝나야하고 @Configuration 에너테이션이 있어야 한다")
  void configTest() {
    ArchRule rule =
        classes()
            .that()
            .resideInAnyPackage("..config..")
            .should()
            .haveSimpleNameEndingWith("Config")
            .andShould()
            .beAnnotatedWith(Configuration.class);

    rule.check(javaClasses);
  }

  @FastTest
  @DisplayName("Controller는 Service와 Request/Response를 사용할 수 있음")
  void controllerDependencyTest() {
    ArchRule rule =
        classes()
            .that()
            .resideInAnyPackage("..controller")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..request..", "..response..", "..service..");

    rule.check(javaClasses);
  }

  @FastTest
  @DisplayName("Controller는 의존되지 않음")
  void controllerDependencyTest2() {
    ArchRule rule =
        classes()
            .that()
            .resideInAnyPackage("..controller")
            .should()
            .onlyHaveDependentClassesThat()
            .resideInAnyPackage("..controller");

    rule.check(javaClasses);
  }

  @FastTest
  @DisplayName("Controller는 모델을 사용할 수 없음")
  void controllerDependencyTest3() {
    ArchRule rule =
        noClasses() // 부정을 의미한다.
            .that()
            .resideInAnyPackage("..controller")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..model..");

    rule.check(javaClasses);
  }

  @FastTest
  @DisplayName("Service는 Controller를 의존하면 안됨")
  void serviceDependencyTest() {
    ArchRule rule =
        noClasses()
            .that()
            .resideInAnyPackage("..service..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..controller");

    rule.check(javaClasses);
  }

  @FastTest
  @DisplayName("Model은 오직 Service와 Repository에 의해 의존됨")
  void modelDependencyTest() {
    ArchRule rule =
        classes()
            .that()
            .resideInAnyPackage("..model..")
            .should()
            .onlyHaveDependentClassesThat()
            .resideInAnyPackage("..repository..", "..service..", "..model..");

    rule.check(javaClasses);
  }

  @FastTest
  @DisplayName("Model은 아무것도 의존하지 않음")
  void modelDependencyTest2() {
    ArchRule rule =
        classes()
            .that()
            .resideInAnyPackage("..model..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage("..model..", "java..", "jakarta..");

    rule.check(javaClasses);
  }
}
