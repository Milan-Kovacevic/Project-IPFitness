USE `ip_fitness`;

LOCK TABLES `administrator` WRITE;
INSERT INTO `administrator` VALUES 
(1,'admin','123','Milan','Kovacevic'),
(2,'admin_backup','123','Backup','Administrator');
UNLOCK TABLES;

LOCK TABLES `advisor` WRITE;
INSERT INTO `advisor` VALUES 
(1,'aleksa','123','Aleksa Janković','aleksa_jankovic@mail.com',1),
(2,'marija','123','Marija Maric','marija_maric@mail.com',2),
(3,'tamara','12345','Tamara Petkovic','tamara_petkovic@mail.com',1);
UNLOCK TABLES;

LOCK TABLES `fitness_user` WRITE;
INSERT INTO `fitness_user` VALUES 
(1,'marko@mail.com','Banja Luka','MTcwNjEyMTEwMzYwNF9JUEY=.jpg','marko','$2a$10$l3SAScyqAIhqcF1cUwIbUOn.xf52hfZrFbUIIiNOOwkJRurKp9viy','Marko','Marković',1,1,'I am a dedicated and passionate fitness enthusiast who has spent years inspiring others to lead healthier and more active lives. Born in 1998 in Banja Luka, my journey into the world of fitness began at a young age when I discovered a profound love for sports and physical activity.','marko@mail.com'),
(2,'janko@mail.com','Laktaši','MTcwNjEyMTI2OTA5OV9JUEY=.jpg','janko','$2a$10$yVDWRPGm7fdrp8C27UwNcelw1Cd3R7fdFaUdM4bewFvPaKVOXXfiO','Janko','Janković',1,1,'As a fitness enthusiast, my journey into the realm of health and wellness started with a genuine passion for leading an active lifestyle. From my early days, I found joy and fulfillment in exploring various sports and physical activities, laying the foundation for a lifelong commitment to fitness.','001/250-7890'),
(3,'petar@mail.com','Gradiška','MTcwNjEyMTM1MzcxNV9JUEY=.jpg','petar','$2a$10$v7XMBITeYOAqIXVuFmq.hOB/ahymfN7qLpm6hvsFYTdO12W3XZH3C','Petar','Petrović',1,1,NULL,NULL),
(4,'nemanja@mail.com','Banja Luka','MTcwNjEyMTUzMjgxNl9JUEY=.jpg','nemanja','$2a$10$BOCO.qaoyh9XAbcHymcAPeIVDjmv2iL6jPddPSYNStTFjziHWhws.','Nemanja','Nemanjić',0,1,NULL,NULL),
(5,'nikola@mail.com','Banja Luka','MTcwNjEyMTU4NTA1Nl9JUEY=.jpg','nikola','$2a$10$1XvlJZBLZXgFKmsWyRSPsuYU.zu17wtbiBSTcJBNac9iNyqek8a0u','Nikola','Nikolić',1,1,NULL,NULL);
UNLOCK TABLES;

LOCK TABLES `category` WRITE;
INSERT INTO `category` VALUES 
(4,'Cardio'),
(6,'CrossFit'),
(5,'Functional Fitness'),
(9,'HIIT'),
(7,'Mindfulness'),
(8,'Pilates'),
(10,'Senior Fitness'),
(2,'Strength Training'),
(1,'Weight Loss'),
(3,'Yoga');
UNLOCK TABLES;

LOCK TABLES `category_subscription` WRITE;
INSERT INTO `category_subscription` VALUES 
(3,1),
(2,2),
(3,2),
(2,3),
(3,3),
(1,4),
(1,5),
(2,6),
(1,7),
(2,7),
(1,9),
(2,9),
(2,10);
UNLOCK TABLES;

LOCK TABLES `category_attribute` WRITE;
INSERT INTO `category_attribute` VALUES 
(1,'Type of activity',4),
(2,'Type of exercise equipment',2),
(3,'Weight',2);
UNLOCK TABLES;

LOCK TABLES `fitness_program` WRITE;
INSERT INTO `fitness_program` VALUES 
(1,'Corporate Wellness','Tailored fitness for workplace wellness',49.99,0,'2024-01-01 00:00:00','2025-01-01 00:00:00',0,10,2,0,'2024-01-01 17:00:19'),
(2,'Core Sculpting','Isolate and strengthen your core muscles',34.99,1,'2024-04-01 00:00:00','2024-04-30 00:00:00',3,2,3,0,'2024-01-01 23:58:19'),
(3,'Meditation Movement','Combine meditation with gentle movement',24.99,0,'2024-01-08 00:00:00','2024-01-29 00:00:00',0,7,2,0,'2024-01-02 00:17:28'),
(4,'Muscle Building 101','Build muscle and strength',59.99,2,'2024-01-15 00:00:00','2024-04-15 00:00:00',1,2,1,0,'2024-01-10 14:23:58'),
(5,'CrossFit Challenge','CrossFit-style workouts for all levels',30.00,2,'2024-01-15 00:00:00','2024-01-31 00:00:00',1,6,5,0,'2024-01-12 10:26:12'),
(6,'Senior Fitness','Tailored fitness for seniors',14.99,0,'2024-01-15 00:00:00','2024-01-22 00:00:00',0,10,4,0,'2024-01-15 15:39:53'),
(7,'Weight Loss Journey','Intensive weight loss program',45.99,1,'2024-02-01 00:00:00','2024-03-31 00:00:00',1,1,1,0,'2024-01-15 20:10:13'),
(8,'Kickboxing Challenge','High-energy kickboxing workouts',44.99,2,'2024-03-15 00:00:00','2024-04-15 00:00:00',1,5,4,0,'2024-01-16 08:41:42'),
(9,'Flexibility Flow','Improve flexibility through dynamic movements',20.00,1,'2024-01-16 00:00:00','2024-01-31 00:00:00',4,3,5,0,'2024-01-16 10:05:27'),
(10,'Cardio Blast','High-intensity cardio workouts',39.99,1,'2024-03-15 00:00:00','2024-05-15 00:00:00',2,1,3,0,'2024-01-18 23:03:01'),
(11,'Running Club','Group running sessions for all levels',19.99,0,'2024-02-01 00:00:00','2024-02-29 00:00:00',2,4,3,0,'2024-01-20 22:32:09'),
(12,'HIIT Revolution','High-Intensity Interval Training',42.99,2,'2024-02-01 00:00:00','2024-02-29 00:00:00',0,9,1,0,'2024-01-21 12:20:00'),
(13,'Powerlifting Mastery','Focus on the big three lifts',54.99,2,'2024-01-31 00:00:00','2024-03-15 00:00:00',1,2,2,0,'2024-01-21 20:04:29'),
(14,'Zumba Fiesta','Dance-based fitness for fun and energy',29.99,1,'2024-01-10 00:00:00','2024-02-10 00:00:00',6,6,2,0,'2024-01-21 21:26:39'),
(15,'Pilates Fusion','Pilates with a mix of other exercises',39.99,1,'2024-05-15 00:00:00','2024-07-15 00:00:00',3,8,2,0,'2024-01-22 16:41:28'),
(16,'Yoga for Beginners','Introduction to yoga practices',29.99,0,'2024-02-01 00:00:00','2024-04-30 00:00:00',4,3,2,0,'2024-01-23 16:25:04'),
(17,'Cycling Adventures','Explore scenic routes on a bike',32.99,1,'2024-03-15 00:00:00','2024-04-15 00:00:00',5,4,1,0,'2024-01-23 18:25:07'),
(18,'Mindful Movement','Mind-body connection through movement',34.99,0,'2024-01-25 00:00:00','2024-03-25 00:00:00',0,7,3,0,'2024-01-24 09:12:01'),
(19,'Functional Fitness','Improve daily functional movements',44.99,1,'2024-01-01 00:00:00','2024-02-29 00:00:00',3,5,2,0,'2023-12-29 06:57:39');
UNLOCK TABLES;

LOCK TABLES `attribute_value` WRITE;
INSERT INTO `attribute_value` VALUES 
(1,1,17,'Cycling'),
(2,1,11,'Running'),
(3,2,2,'Dumbbell'),
(4,3,2,'10kg'),
(5,2,4,'Straight standard bar'),
(6,3,4,'100kg');
UNLOCK TABLES;

LOCK TABLES `activity` WRITE;
INSERT INTO `activity` VALUES 
(1,2,'Running','2024-01-02 09:30:00',40,95,85.000,'Morning jog in the park'),
(2,2,'Weight Lifting','2024-01-03 21:00:00',60,85,85.250,'Focused on upper body strength'),
(3,2,'Yoga','2024-01-04 18:30:00',45,100,85.500,'Relaxing yoga session'),
(4,2,'Running','2024-01-05 15:45:00',50,90,85.300,'Interval running training'),
(5,2,'Weight Lifting','2024-01-06 20:15:00',55,80,85.750,'Leg day focus'),
(6,2,'Cycling','2024-01-08 16:00:00',40,100,86.200,'Scenic cycling route'),
(7,2,'HIIT','2024-01-10 19:30:00',30,85,86.500,'High-intensity interval training'),
(8,2,'Yoga','2024-01-11 18:45:00',50,95,86.800,'Vinyasa flow yoga'),
(9,2,'Running','2024-01-13 15:15:00',55,90,87.100,'Long-distance run'),
(10,2,'Weight Lifting','2024-01-15 20:00:00',60,80,87.400,'Full-body strength workout'),
(11,2,'Pilates','2024-01-16 17:30:00',45,100,87.700,'Pilates core session'),
(12,2,'Cycling','2024-01-18 16:15:00',40,95,88.000,'Bike trail exploration'),
(13,2,'Running','2024-01-20 15:45:00',50,90,88.300,'Speed workout'),
(14,2,'Yoga','2024-01-21 18:30:00',55,85,88.600,'Restorative yoga'),
(15,2,'HIIT','2024-01-22 19:00:00',30,100,89.000,'Tabata training'),
(16,2,'Weight Lifting','2024-01-23 20:15:00',60,90,89.000,'Strength and endurance focus'),
(17,2,'Running','2024-01-24 15:30:00',45,85,89.300,'Trail run');
UNLOCK TABLES;

LOCK TABLES `comment` WRITE;
INSERT INTO `comment` VALUES 
(1,'Which muscle group targets this fitness program?','2024-01-24 21:22:56',2,4),
(2,'Hello there, it targets primary lower back and latissimus','2024-01-25 09:18:21',1,4),
(3,'Hello, I would like to purchase this fitness program, but I can only follow it thorough the end of February, is that ok?','2024-01-26 15:19:41',3,4),
(4,'Yeah, but unfortunately you need to pay the full price of program.\nBest Regards,\nMarko Markovic.','2024-01-26 16:06:01',1,4),
(5,'Hello there.','2024-01-24 21:29:12',2,2),(6,'Looks fun :)','2024-01-24 21:31:54',2,17),
(7,'Nice explanation of the workout!','2024-01-24 21:38:29',1,1);
UNLOCK TABLES;


LOCK TABLES `message` WRITE;
INSERT INTO `message` VALUES 
(1,'Hi there!','2024-01-24 21:46:47',2,1),
(2,'How are you?','2024-01-24 21:46:55',2,1),
(3,'I am fine, thank you for asking.','2024-01-24 21:47:21',1,2),
(4,'Have you done your workout for today?','2024-01-24 21:47:37',1,2),
(5,'Yes I have, and you?','2024-01-24 21:47:54',2,1),
(6,'Hi there!','2024-01-24 21:48:06',2,3),
(7,'Hello Petar!','2024-01-24 21:48:17',2,3),
(8,'Yeah','2024-01-24 21:57:28',1,2);
UNLOCK TABLES;

LOCK TABLES `program_demonstration` WRITE;
INSERT INTO `program_demonstration` VALUES 
(1,'https://www.youtube.com/watch?v=jpizoUy4K9s',0,12),
(2,'https://www.youtube.com/watch?v=tKu6oA33f34',1,12),
(4,'https://www.youtube.com/watch?v=zwMq2VMUgM0',0,1),
(5,'https://www.youtube.com/watch?v=loMs6iangGs',0,3),
(6,'https://www.youtube.com/watch?v=ZJAOlpQcwXg',1,3);
UNLOCK TABLES;

LOCK TABLES `program_picture` WRITE;
INSERT INTO `program_picture` VALUES 
(1,'MTcwNjEyNjMxMzc2M19JUEY=.jpg',0,4),
(2,'MTcwNjEyNjMxMzc3M19JUEY=.jpg',1,4),
(3,'MTcwNjEyNjM2MjQyOV9JUEY=.jpg',0,7),
(4,'MTcwNjEyNjQ0OTcwMF9JUEY=.jpg',0,12),
(5,'MTcwNjEyNjQ0OTcyMl9JUEY=.jpg',1,12),
(6,'MTcwNjEyNjUzMTQyMV9JUEY=.jpg',0,17),
(7,'MTcwNjEyNjc4MDg3OF9JUEY=.jpg',0,1),
(8,'MTcwNjEyNjkyODkxMV9JUEY=.jpg',0,3),
(9,'MTcwNjEyNjkyODk0OF9JUEY=.jpg',1,3),
(10,'MTcwNjEyNjk3OTY0OV9JUEY=.jpg',0,13),
(11,'MTcwNjEyNjk3OTY3NV9JUEY=.jpg',1,13),
(12,'MTcwNjEyNzA2ODgyNF9JUEY=.jpg',0,19),
(13,'MTcwNjEyNzU2NjIxNV9JUEY=.jpg',0,11),
(14,'MTcwNjEyNzU2NjIyOV9JUEY=.jpg',1,11);
UNLOCK TABLES;

LOCK TABLES `program_purchase` WRITE;
INSERT INTO `program_purchase` VALUES 
(1,0,49.99,1,1,'2024-01-24 21:37:54'),
(2,3,59.99,2,4,'2024-01-24 21:42:22'),
(3,1,32.99,2,17,'2024-01-24 21:43:34'),
(4,1,42.99,2,12,'2024-01-24 21:44:39');
UNLOCK TABLES;

LOCK TABLES `question` WRITE;
INSERT INTO `question` VALUES 
(1,1,0,'Sending test mail 1...','2024-01-23 14:26:59'),
(2,1,0,'Sending test mail 2...','2024-01-24 21:55:01'),
(3,2,1,'Sending mail from Janko','2024-01-25 17:24:20'),
(4,3,0,'Hello advisor','2024-01-25 18:05:42'),
(5,2,0,'Test mail','2024-01-26 21:10:42');
UNLOCK TABLES;
