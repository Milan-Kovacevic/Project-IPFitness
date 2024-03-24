package dev.projectfitness.advisor.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Part;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import dev.projectfitness.advisor.beans.MessageBean;
import dev.projectfitness.advisor.dtos.Message;
import dev.projectfitness.advisor.dаоs.DaoFactory;
import dev.projectfitness.advisor.dаоs.MessageDao;
import dev.projectfitness.advisor.util.Constants;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class MessageService {
	private final MessageDao messageDao;

	public MessageService() {
		super();
		messageDao = DaoFactory.getInstance().getMessageDao();
	}

	public List<MessageBean> getAllUnreadMessages() {
		return messageDao.getAllUnread().stream().map(x -> new MessageBean(x)).collect(Collectors.toList());
	}

	public List<MessageBean> searchUnreadMessages(String query) {
		return messageDao.searchUnreadMessages(query).stream().map(x -> new MessageBean(x))
				.collect(Collectors.toList());
	}

	public MessageBean getMessage(Integer questionId) {
		Optional<Message> result = messageDao.getById(questionId);
		if (result.isEmpty())
			return null;
		messageDao.updateMessageRead(questionId);
		return new MessageBean(result.get());
	}

	public int sendMessageResponse(Message originalMessage, String subject, String body, List<Part> attachment) {
		String emailFrom = "noreply.ipfitness@gmail.com";
		String sendTo = originalMessage.getSenderMail();

		Client client = ClientBuilder.newClient().register(MultiPartFeature.class);
		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.field("emailFrom", emailFrom).field("sendTo", sendTo).field("subject", subject).field("body", body);

		for (Part part : attachment) {
			try {
				InputStream is = part.getInputStream();
				String originalFileName = part.getSubmittedFileName();
				var bodyPart = new FormDataBodyPart("attachmentFiles", new ByteArrayInputStream(is.readAllBytes()),
						MediaType.APPLICATION_OCTET_STREAM_TYPE);

				bodyPart.getHeaders().add("Content-Disposition",
						"form-data; name=\"attachmentFiles\"; filename=\"" + originalFileName + "\"");
				multiPart.bodyPart(bodyPart);

				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		int status = HttpURLConnection.HTTP_NOT_FOUND;
		try {
			WebTarget target = client.target(Constants.EMAIL_SERVICE_ENDPOINT);
			Response response = target.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));
			status = response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpURLConnection.HTTP_UNAVAILABLE;
		}

		return status;
	}
}
