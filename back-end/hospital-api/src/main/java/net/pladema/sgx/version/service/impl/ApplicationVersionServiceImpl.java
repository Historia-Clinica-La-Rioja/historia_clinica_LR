package net.pladema.sgx.version.service.impl;

import net.pladema.sgx.version.service.ApplicationVersionService;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Service
public class ApplicationVersionServiceImpl implements ApplicationVersionService {

    private final Logger LOG = LoggerFactory.getLogger(ApplicationVersionServiceImpl.class);

    @Override
    public String getCurrentVersion() {
        LOG.debug("No input parameters");
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader("./pom-parent.xml"));
            String snapshot = (String) model.getProperties().get("revision");
            String version = snapshot.replace("-SNAPSHOT","");
            LOG.debug("Output -> {}", version);
            return version;
        }
        catch(IOException | XmlPullParserException ex){
            String result = "";
            LOG.debug("Output -> {}", result);
            return result;
        }
    }

}
