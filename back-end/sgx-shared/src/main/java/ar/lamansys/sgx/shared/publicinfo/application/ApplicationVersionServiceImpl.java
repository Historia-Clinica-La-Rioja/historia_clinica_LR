package ar.lamansys.sgx.shared.publicinfo.application;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class ApplicationVersionServiceImpl implements ApplicationVersionService {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationVersionServiceImpl.class);

    private static final String NO_INPUT_PARAMETERS = "No input parameters";

    private static final String OUTPUT = "Output -> {}";

    @Override
    public String getCurrentVersion() {
        LOG.debug(NO_INPUT_PARAMETERS);
        try {
            String version = getVersionFromPomParentFile();
            LOG.debug(OUTPUT, version);
            return version;
        }
        catch(IOException | XmlPullParserException pomParentReadException){
            try {
                String version = getVersionFromPomPropertiesFile();
                LOG.debug(OUTPUT, version);
                return version;
            }
            catch(IOException pomPropertiesReadException){
                String result = "";
                LOG.debug(OUTPUT, result);
                return result;
            }
        }
    }

    private String getVersionFromPomParentFile() throws IOException, XmlPullParserException {
        LOG.debug(NO_INPUT_PARAMETERS);
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("./pom-parent.xml"));
        String version = (String) model.getProperties().get("revision");
        LOG.debug(OUTPUT, version);
        return version;
    }

    private String getVersionFromPomPropertiesFile() throws IOException {
        LOG.debug(NO_INPUT_PARAMETERS);
        String version = "";
        Properties properties = new Properties();
        InputStream pomFile = getClass().getClassLoader().getResourceAsStream("pom.properties");
        if (pomFile != null) {
            properties.load(pomFile);
            version = properties.getProperty("version", "");
        }
        LOG.debug(OUTPUT, version);
        return version;
    }

}
