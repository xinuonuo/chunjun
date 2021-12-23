package com.dtstack.flinkx.pulsar.reader;

import com.dtstack.flinkx.config.DataTransferConfig;
import com.dtstack.flinkx.config.ReaderConfig;
import com.dtstack.flinkx.decoder.DecodeEnum;
import com.dtstack.flinkx.pulsar.format.PulsarInputFormat;
import com.dtstack.flinkx.pulsar.format.PulsarInputFormatBuilder;
import com.dtstack.flinkx.reader.BaseDataReader;
import com.dtstack.flinkx.reader.MetaColumn;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;

import java.util.List;
import java.util.Map;

import static com.dtstack.flinkx.pulsar.format.Constants.*;

/**
 * The reader of pulsar. Can reader field dellimitered data like '1001,LeBron James,36,Lakers'
 * with column 'id,name,age,team'.
 * Company: www.dtstack.com
 *
 * @author fengjiangtao_yewu@cmss.chinamobile.com 2021/3/23
 */
public class PulsarReader extends BaseDataReader {

    protected String token;
    protected String topic;
    protected String codec;
    protected String initialPosition;
    protected String fieldDelimiter;
    protected boolean blankIgnore;
    protected int timeout;
    protected String pulsarServiceUrl;
    protected List<MetaColumn> metaColumns;
    protected Map<String, Object> consumerSettings;
    protected String listenerName;
    protected int batchInterval;
    protected int batchBytes;
    protected int batchTime;

    public PulsarReader(DataTransferConfig config, StreamExecutionEnvironment env) {
        super(config, env);
        ReaderConfig readerConfig = config.getJob().getContent().get(0).getReader();

        token = readerConfig.getParameter().getStringVal(KEY_TOKEN);
        topic = readerConfig.getParameter().getStringVal(KEY_TOPIC);
        codec = readerConfig.getParameter().getStringVal(KEY_CODEC, DecodeEnum.TEXT.getName());
        fieldDelimiter = readerConfig.getParameter().getStringVal(KEY_FIELD_DELIMITER, DEFAULT_FIELD_DELIMITER);
        initialPosition = readerConfig.getParameter().getStringVal(KEY_INITIAL_POSITION, SubscriptionInitialPosition.Latest.name());
        blankIgnore = readerConfig.getParameter().getBooleanVal(KEY_BLANK_IGNORE, Boolean.FALSE);
        timeout = readerConfig.getParameter().getIntVal(KEY_TIMEOUT, DEFAULT_TIMEOUT);
        pulsarServiceUrl = readerConfig.getParameter().getStringVal(KEY_PULSAR_SERVICE_URL);
        metaColumns = MetaColumn.getMetaColumns(readerConfig.getParameter().getColumn());
        consumerSettings = (Map<String, Object>) readerConfig.getParameter().getVal(KEY_CONSUMER_SETTINGS);
        listenerName = readerConfig.getParameter().getStringVal(KEY_LISTENER_NAME);
        batchInterval = readerConfig.getParameter().getIntVal(KEY_BATCH_INTERVAL, DEFAULT_BATCH_INTERVAL);
        batchBytes = readerConfig.getParameter().getIntVal(KEY_BATCH_BYTES, DEFAULT_BATCH_BYTES);
        batchTime = readerConfig.getParameter().getIntVal(KEY_BATCH_TIME, DEFAULT_BATCH_TIME);
    }

    @Override
    public DataStream<Row> readData() {
        PulsarInputFormatBuilder builder = new PulsarInputFormatBuilder(new PulsarInputFormat());
        builder.setTopic(topic);
        builder.setToken(token);
        builder.setCodec(codec);
        builder.setTimeout(timeout);
        builder.setPulsarServiceUrl(pulsarServiceUrl);
        builder.setConsumerSettings(consumerSettings);
        builder.setMetaColumns(metaColumns);
        builder.setBlankIgnore(blankIgnore);
        builder.setInitialPosition(initialPosition);
        builder.setFieldDelimiter(fieldDelimiter);
        builder.setDataTransferConfig(dataTransferConfig);
        builder.setListenerName(listenerName);
        builder.setBatchInterval(batchInterval);
        builder.setBatchBytes(batchBytes);
        builder.setBatchTime(batchTime);

        return createInput(builder.finish());
    }
}

