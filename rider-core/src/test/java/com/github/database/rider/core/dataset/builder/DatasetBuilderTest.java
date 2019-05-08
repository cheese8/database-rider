package com.github.database.rider.core.dataset.builder;

import com.github.database.rider.core.dataset.writer.JSONWriter;
import com.github.database.rider.core.dataset.writer.YMLWriter;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.builder.ColumnSpec;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import static com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text.NEW_LINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

@RunWith(JUnit4.class)
public class DatasetBuilderTest {


    @Test
    public void shouldGenerateYamlDataSet() throws DataSetException, IOException {
        RiderDataSetBuilder builder = new RiderDataSetBuilder();
        ColumnSpec<Integer> id = ColumnSpec.newColumn("ID");
        builder.newRow("USER").with("ID", 1)
                .with("NAME", "@realpestano")
                .add().newRow("USER")
                .with(id, 2).with("NAME", "@dbunit")
                .add().newRow("TWEET")
                .with("ID", "abcdef12345").with("CONTENT", "dbunit rules!")
                .with("DATE", "[DAY,NOW]")
                .add().newRow("FOLLOWER").with(id, 1)
                .with("USER_ID", 1).with("FOLLOWER_ID", 2)
                .add().build();

        IDataSet dataSet = builder.build();

        File datasetFile = Files.createTempFile("rider-dataset", ".yml").toFile();
        FileOutputStream fos = new FileOutputStream(datasetFile);
        new YMLWriter(fos).write(dataSet);

        assertThat(contentOf(datasetFile)).
                contains("FOLLOWER:" + NEW_LINE +
                        "  - ID: 1" + NEW_LINE +
                        "    USER_ID: 1" + NEW_LINE +
                        "    FOLLOWER_ID: 2" + NEW_LINE).
                contains("USER:" + NEW_LINE +
                "  - ID: 1" + NEW_LINE +
                "    NAME: \"@realpestano\"" + NEW_LINE +
                "  - ID: 2" + NEW_LINE +
                "    NAME: \"@dbunit\"").
                contains("TWEET:" + NEW_LINE +
                "  - ID: \"abcdef12345\"" + NEW_LINE +
                "    CONTENT: \"dbunit rules!\"" + NEW_LINE +
                "    DATE: \"[DAY,NOW]\""+ NEW_LINE );
    }

    @Test
    public void shouldGenerateJsonDataSet() throws DataSetException, IOException {
        RiderDataSetBuilder builder = new RiderDataSetBuilder();
        ColumnSpec<Integer> id = ColumnSpec.newColumn("ID");
        builder.newRow("USER").with("ID", 1)
                .with("NAME", "@realpestano")
                .add().newRow("USER")
                .with(id, 2).with("NAME", "@dbunit")
                .add().newRow("TWEET")
                .with("ID", "abcdef12345").with("CONTENT", "dbunit rules!")
                .with("DATE", "[DAY,NOW]")
                .add().newRow("FOLLOWER").with(id, 1)
                .with("USER_ID", 1).with("FOLLOWER_ID", 2)
                .add().build();

        IDataSet dataSet = builder.build();

        File datasetFile = Files.createTempFile("rider-dataset", ".json").toFile();
        FileOutputStream fos = new FileOutputStream(datasetFile);
        new JSONWriter(fos,dataSet).write();

        assertThat(contentOf(datasetFile)).
                contains("{"+NEW_LINE  +
                        "  \"USER\": ["+NEW_LINE  +
                        "    {"+NEW_LINE  +
                        "      \"ID\": 1,"+NEW_LINE  +
                        "      \"NAME\": \"@realpestano\""+NEW_LINE  +
                        "    },"+NEW_LINE  +
                        "    {"+NEW_LINE  +
                        "      \"ID\": 2,"+NEW_LINE  +
                        "      \"NAME\": \"@dbunit\""+NEW_LINE  +
                        "    }"+NEW_LINE  +
                        "  ],"+NEW_LINE  +
                        "  \"TWEET\": ["+NEW_LINE  +
                        "    {"+NEW_LINE  +
                        "      \"ID\": \"abcdef12345\","+NEW_LINE  +
                        "      \"CONTENT\": \"dbunit rules!\","+NEW_LINE  +
                        "      \"DATE\": \"[DAY,NOW]\""+NEW_LINE  +
                        "    }"+NEW_LINE  +
                        "  ],"+NEW_LINE  +
                        "  \"FOLLOWER\": ["+NEW_LINE  +
                        "    {"+NEW_LINE  +
                        "      \"ID\": 1,"+NEW_LINE  +
                        "      \"USER_ID\": 1,"+NEW_LINE  +
                        "      \"FOLLOWER_ID\": 2"+NEW_LINE  +
                        "    }"+NEW_LINE  +
                        "  ]"+NEW_LINE  +
                        "}");
    }

    @Test
    public void shouldGenerateFlatXmlDataSet() throws DataSetException, IOException {
        RiderDataSetBuilder builder = new RiderDataSetBuilder();
        ColumnSpec<Integer> id = ColumnSpec.newColumn("ID");
        builder.newRow("USER").with("ID", 1)
                .with("NAME", "@realpestano")
                .add().newRow("USER")
                .with(id, 2).with("NAME", "@dbunit")
                .add().newRow("TWEET")
                .with("ID", "abcdef12345").with("CONTENT", "dbunit rules!")
                .with("DATE", "[DAY,NOW]")
                .add().newRow("FOLLOWER").with(id, 1)
                .with("USER_ID", 1).with("FOLLOWER_ID", 2)
                .add().build();

        IDataSet dataSet = builder.build();

        File datasetFile = Files.createTempFile("rider-dataset", ".xml").toFile();
        FileOutputStream fos = new FileOutputStream(datasetFile);
        FlatXmlDataSet.write(dataSet, fos);
        assertThat(contentOf(datasetFile)).
                contains("<?xml version='1.0' encoding='UTF-8'?>"+NEW_LINE  +
                        "<dataset>"+NEW_LINE  +
                        "  <USER ID=\"1\" NAME=\"@realpestano\"/>"+NEW_LINE  +
                        "  <USER ID=\"2\" NAME=\"@dbunit\"/>"+NEW_LINE  +
                        "  <TWEET ID=\"abcdef12345\" CONTENT=\"dbunit rules!\" DATE=\"[DAY,NOW]\"/>"+NEW_LINE  +
                        "  <FOLLOWER ID=\"1\" USER_ID=\"1\" FOLLOWER_ID=\"2\"/>"+NEW_LINE  +
                        "</dataset>");

    }
}
