package org.apache.giraph.debugger.examples.integrity;

import org.apache.giraph.debugger.DebugConfig;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;

/**
 * Debug configuration file for ConnectedComponents, that is configured to check the integrity
 * of the vertex values: The current check is that the vertex value is less than or equal to the
 * id of the vertex.
 * 
 * @author semihsalihoglu
 */
public class ConnectedComponentsVValueIntegrityDebugConfig extends DebugConfig<IntWritable,
  IntWritable, NullWritable, IntWritable, IntWritable> {

  @Override
  public boolean shouldCheckVertexValueIntegrity() {
    return true;
  }
  
  @Override
  public boolean isVertexValueCorrect(IntWritable vertexId, IntWritable value) {
    return value.get() <= vertexId.get();
  }
}
