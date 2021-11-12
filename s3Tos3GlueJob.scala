import com.amazonaws.services.glue.GlueContext
import com.amazonaws.services.glue.MappingSpec
import com.amazonaws.services.glue.errors.CallSite
import com.amazonaws.services.glue.util.GlueArgParser
import com.amazonaws.services.glue.util.Job
import com.amazonaws.services.glue.util.JsonOptions
import org.apache.spark.SparkContext
import scala.collection.JavaConverters._

object GlueApp {
  def main(sysArgs: Array[String]) {
    val spark: SparkContext = new SparkContext()
    val glueContext: GlueContext = new GlueContext(spark)
    // @params: [JOB_NAME]
    val args = GlueArgParser.getResolvedOptions(sysArgs, Seq("JOB_NAME").toArray)
    Job.init(args("JOB_NAME"), glueContext, args.asJava)
    // Script generated for node S3 bucket
    val S3bucket_node1 = glueContext.getSourceWithFormat(formatOptions=JsonOptions("""{"quoteChar": "\"", "withHeader": true, "separator": ","}"""), connectionType="s3", format="csv", options=JsonOptions("""{"paths": ["s3://test-bucket-glue-ankit/input/l2s2.csv"], "recurse": true}"""), transformationContext="S3bucket_node1").getDynamicFrame()

    // Script generated for node ApplyMapping
    val ApplyMapping_node2 = S3bucket_node1.applyMapping(mappings=Seq(("id", "long", "id", "long"), ("col1", "long", "col1", "long"), ("col2", "long", "col2", "long"), ("col3", "string", "col3", "string"), ("col4", "string", "col4", "string")), caseSensitive=false, transformationContext="ApplyMapping_node2")

    // Script generated for node S3 bucket
    val S3bucket_node3 = glueContext.getSinkWithFormat(connectionType="s3", options=JsonOptions("""{"path": "s3://test-bucket-glue-ankit/output/", "partitionKeys": []}"""), transformationContext="S3bucket_node3", format="json").writeDynamicFrame(ApplyMapping_node2)

    Job.commit()
  }
}
