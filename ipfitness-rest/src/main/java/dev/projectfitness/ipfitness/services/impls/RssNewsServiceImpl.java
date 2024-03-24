package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.models.dtos.RssNews;
import dev.projectfitness.ipfitness.models.dtos.RssNewsItem;
import dev.projectfitness.ipfitness.services.RssNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class RssNewsServiceImpl implements RssNewsService {
    @Value("${ipfitness.rss.news-url}")
    private String rssNewsEndpoint;

    private static final String ITEM_ELEMENT = "item";
    private static final String TITLE_ELEMENT = "title";
    private static final String LINK_ELEMENT = "link";
    private static final String IMAGE_ELEMENT = "image";
    private static final String DESCRIPTION_ELEMENT = "description";
    private static final String COPYRIGHT_ELEMENT = "copyright";
    private static final String CATEGORY_ELEMENT = "category";

    @Override
    public RssNews getAllFitnessNews() {
        try {
            RssNews news = new RssNews();

            URL rssUrl = new URL(rssNewsEndpoint);
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = rssUrl.openStream();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            String title = "", link = "", image = "", description = "", copyright = "", category = "";

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    if (ITEM_ELEMENT.equalsIgnoreCase(localPart)) {
                        event = eventReader.nextEvent();
                        news.setTitle(title);
                        news.setLink(link);
                        news.setImage(image);
                        news.setDescription(description);
                        news.setCopyright(copyright);
                    } else if (TITLE_ELEMENT.equalsIgnoreCase(localPart)) {
                        title = getRssStreamData(eventReader);
                    } else if (LINK_ELEMENT.equalsIgnoreCase(localPart)) {
                        link = getRssStreamData(eventReader);
                    } else if (IMAGE_ELEMENT.equalsIgnoreCase(localPart)) {
                        image = getRssStreamData(eventReader);
                    } else if (DESCRIPTION_ELEMENT.equalsIgnoreCase(localPart)) {
                        description = getRssStreamData(eventReader);
                    } else if (COPYRIGHT_ELEMENT.equalsIgnoreCase(localPart)) {
                        copyright = getRssStreamData(eventReader);
                    } else if (CATEGORY_ELEMENT.equalsIgnoreCase(localPart)) {
                        category = getRssStreamData(eventReader);
                    }
                } else if (event.isEndElement()) {
                    if (ITEM_ELEMENT.equalsIgnoreCase(event.asEndElement().getName().getLocalPart())) {
                        RssNewsItem newsItem = new RssNewsItem();
                        newsItem.setCategory(category);
                        newsItem.setTitle(title);
                        newsItem.setDescription(description);
                        newsItem.setLink(link);
                        news.getItems().add(newsItem);
                        event = eventReader.nextEvent();
                    }
                }
            }
            in.close();
            return news;
        } catch (Exception ex) {
            // TODO: Log
            throw new NotFoundException();
        }
    }

    private String getRssStreamData(XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        XMLEvent event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }
}
