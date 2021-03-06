package org.apache.giraph.debugger.mock;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Set;

import org.apache.giraph.utils.WritableUtils;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class FormatHelper {

  private final DecimalFormat decimalFormat = new DecimalFormat("#.#####");
  
  @SuppressWarnings("rawtypes")
  private Set<Class> complexWritables;
  
  public FormatHelper() {
  }
  
  @SuppressWarnings("rawtypes")
  public FormatHelper(Set<Class> complexWritables) {
    registerComplexWritableClassList(complexWritables);
  }
  
  @SuppressWarnings("rawtypes")
  public void registerComplexWritableClassList(Set<Class> complexWritables) {
    this.complexWritables = complexWritables;
  }

  public String formatWritable(Writable writable) {
    if (writable instanceof NullWritable) {
      return "NullWritable.get()";
    } else if (writable instanceof BooleanWritable) {
      return String.format("new BooleanWritable(%s)", format(((BooleanWritable) writable).get()));
    } else if (writable instanceof ByteWritable) {
      return String.format("new ByteWritable(%s)", format(((ByteWritable) writable).get()));
    } else if (writable instanceof IntWritable) {
      return String.format("new IntWritable(%s)", format(((IntWritable) writable).get()));
    } else if (writable instanceof LongWritable) {
      return String.format("new LongWritable(%s)", format(((LongWritable) writable).get()));
    } else if (writable instanceof FloatWritable) {
      return String.format("new FloatWritable(%s)", format(((FloatWritable) writable).get()));
    } else if (writable instanceof DoubleWritable) {
      return String.format("new DoubleWritable(%s)", format(((DoubleWritable) writable).get()));
    } else if (writable instanceof Text) {
      return String.format("new Text(%s)", ((Text) writable).toString());
    } else {
      if (complexWritables != null)
        complexWritables.add(writable.getClass());
      String str = toByteArrayString(WritableUtils.writeToByteArray(writable));
      return String.format("(%s)read%sFromByteArray(new byte[] {%s})", writable.getClass()
          .getSimpleName(), writable.getClass().getSimpleName(), str);
    }
  }

  public String format(Object input) {
    if (input instanceof Boolean || input instanceof Byte || input instanceof Character
        || input instanceof Short || input instanceof Integer) {
      return input.toString();
    } else if (input instanceof Long) {
      return input.toString() + "l";
    } else if (input instanceof Float) {
      return decimalFormat.format(input) + "f";
    } else if (input instanceof Double) {
      double val = ((Double) input).doubleValue();
      if (val == Double.MAX_VALUE)
        return "Double.MAX_VALUE";
      else if (val == Double.MIN_VALUE)
        return "Double.MIN_VALUE";
      else {
        BigDecimal bd = new BigDecimal(val);
        return bd.toEngineeringString() + "d";
      }
    } else {
      return input.toString();
    }
  }

  private String toByteArrayString(byte[] byteArray) {
    StringBuilder strBuilder = new StringBuilder();
    for (int i = 0; i < byteArray.length; i++) {
      if (i != 0)
        strBuilder.append(',');
      strBuilder.append(Byte.toString(byteArray[i]));
    }
    return strBuilder.toString();
  }

}
