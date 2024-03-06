package com.webclient1c.release02.tapestry.unit.pages;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.test.PageTester;
import org.junit.jupiter.api.Test;

import com.webclient1c.release02.services.AppModule;
import com.webclient1c.release02.services.QaModule;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndexTest {

    @Test
    void test() {
        // given
        String appPackage = "com.webclient1c.release02";
        String appName = "App";
        PageTester tester = new PageTester(appPackage, appName, "src/main/webapp", AppModule.class, QaModule.class);

        // when
        Document doc = tester.renderPage("Index");

        // then
        assertTrue(doc.getElementById("title").getChildMarkup().startsWith("Simple MVP for products stock level dynamic pivot table experiments."));
    }
}

