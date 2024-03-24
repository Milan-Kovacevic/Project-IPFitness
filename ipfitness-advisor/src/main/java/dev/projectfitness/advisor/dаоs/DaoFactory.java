package dev.projectfitness.advisor.dаоs;

public class DaoFactory {
	private static DaoFactory instance;
	private final AdvisorDao advisorDao;
	private final MessageDao messageDao;
	
	public static DaoFactory getInstance() {
		if (instance == null)
			instance = new DaoFactory();
		return instance;
	}

	private DaoFactory() {
		super();
		advisorDao = new AdvisorDao();
		messageDao = new MessageDao();
	}

	public AdvisorDao getAdvisorDao() {
		return advisorDao;
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

}
