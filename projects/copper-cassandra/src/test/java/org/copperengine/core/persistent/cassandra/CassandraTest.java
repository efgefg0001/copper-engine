package org.copperengine.core.persistent.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.copperengine.core.WorkflowInstanceDescr;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CassandraTest {

    private static final CassandraEngineFactory factory = new CassandraEngineFactory();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        factory.createEngine(true);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        factory.destroyEngine();
    }

    @Test
    public void testParallel() throws Exception {
        List<String> cids = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            final String cid = factory.engine.createUUID();
            final TestData data = new TestData(cid, "foo");
            final WorkflowInstanceDescr<TestData> wfid = new WorkflowInstanceDescr<TestData>("org.copperengine.core.persistent.cassandra.workflows.TestWorkflow", data, cid, 1, null);
            factory.engine.run(wfid);
            cids.add(cid);
        }
        for (String cid : cids) {
            Object response = factory.backchannel.wait(cid, 10000, TimeUnit.MILLISECONDS);
            org.junit.Assert.assertNotNull("no response for workflow instance " + cid, response);
            org.junit.Assert.assertEquals("OK", response);
        }
    }

    @Test
    public void testSerial() throws Exception {
        for (int i = 0; i < 3; i++) {
            final String cid = factory.engine.createUUID();
            final TestData data = new TestData(cid, "foo");
            final WorkflowInstanceDescr<TestData> wfid = new WorkflowInstanceDescr<TestData>("org.copperengine.core.persistent.cassandra.workflows.TestWorkflow", data, cid, 1, null);
            factory.engine.run(wfid);
            Object response = factory.backchannel.wait(cid, 10000, TimeUnit.MILLISECONDS);
            org.junit.Assert.assertNotNull(response);
            org.junit.Assert.assertEquals("OK", response);
        }
    }

}