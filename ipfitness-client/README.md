# Ip Fitness - Client Application
This project was built with Angular version 17.0.8.

### Project Application Requirement
> Fitness Online Application 

*This is the central application of the system used for providing and searching for online fitness programs. The application enables users to browse and participate in various fitness programs. Each program has a name, description, category (e.g., cardio, strength, flexibility, HIIT), basic common attributes (price, difficulty level, duration), location (e.g., online, gym, park), images, instructor information, and contact details. Specific attributes are added for each category (e.g., for cardio - type of activity, for strength - type of equipment or body weight). All possible categories and attributes are defined in the administrative application. Filtering and searching by these criteria are enabled. Displaying all programs simultaneously is not allowed; instead, some form of pagination (classic pagination - pagination, virtual scroll) is supported. Programs are shown as cards, with mandatory display of name, image, and price. Clicking on a card opens details on a new page, showing all information. Questions can be asked, and all conversations for a specific program are displayed to all users as comments.*

*Users without an account can search for programs, view details, but cannot ask questions or participate. An account is created on the registration form where users enter their name, surname, city, username, password, avatar (optional), and email. After filling out the form, a registration confirmation and account activation link are sent to the email, leading the user to the registration endpoint. Optionally provide a Captcha service to confirm that it is a real user, not a bot, such as https://javalite.io/captcha. When the account is successfully created and activated, the user accesses the main page, where all functionalities are available via a menu. If the account is created but not activated, the user receives an activation form upon logging into the system (enters the correct username and password), and then the link is regenerated and sent to the email again. Logged-in users can change their information (except username) on a separate page. Additionally, they can review previous participations in programs, purchases, etc.*

*Participation is done by users choosing a payment method (credit card, PayPal, in-person), and the program is recorded as participated. Detailed processing of the payment method is not required, only basic usage (e.g., entering the card number). Participation implies going to the location, if the fitness program is live, or watching a YouTube video for online fitness programs.
Each user can create a new fitness program that will be available in search to other users. Furthermore, the user can view their programs (active and completed) and delete any of their programs.
Users with an account can send messages to advisors to choose programs from a form located somewhere in the application. When contacting, information about the user and the message content is saved. Also, users can communicate with each other via messages.*

*The homepage of the application displays an RSS feed with the latest news and information from the fitness world by consuming the RSS feed https://feeds.feedburner.com/AceFitFacts. Additionally, the application consumes an API (https://api-ninjas.com/api/exercises) to provide daily suggestions of 10 exercises with instructions to registered users. Exercises are displayed with name, instructions, and level.
Registered users can keep track of their daily activity, monitor workout results and progress. The user can enter information about the type of exercises, duration, intensity, and results. The application provides users with graphical representation of progress, including weight loss, over a certain period. The user can download their activity log as a PDF document.
Users are offered the option to subscribe to a specific category. Subscribed users receive new programs created for that category once a day via email.*

*The application must have a uniform appearance across all pages and must be responsive. Angular and SpringBoot are used for development, along with a database of choice. Ready-made libraries such as Bootstrap or Material are allowed. All functionalities of the SpringBoot application are available via RESTful services.*

### Application Demo
***Note:*** *The quality of the following videos is not the best and does not provide an exact picture of the application.*

![login](https://github.com/Milan-Kovacevic/Project-IPFitness/assets/93384395/15467515-a6d0-40be-9398-f4c45e4fc1c4)

![first](https://github.com/Milan-Kovacevic/Project-IPFitness/assets/93384395/bca82cee-1b53-4f79-a3a0-0a8088cce0ad)

![second](https://github.com/Milan-Kovacevic/Project-IPFitness/assets/93384395/936256d8-f83e-4fec-9bd0-adb31bc1377e)

![third](https://github.com/Milan-Kovacevic/Project-IPFitness/assets/93384395/2e55693b-1f1a-409c-9475-0c9e8ced9aa7)

![forth](https://github.com/Milan-Kovacevic/Project-IPFitness/assets/93384395/4cd56058-e77c-4434-a551-65a40554bc1f)
