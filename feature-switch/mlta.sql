CREATE DATABASE mlta;

USE mlta;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `user` */

insert  into `user`(`id`,`email`) values 

(1,'abcd@gmail.com'),
(2,'xyz@gmail.com');


/*Table structure for table `feature` */

DROP TABLE IF EXISTS `feature`;

CREATE TABLE `feature` (
  `id` int(11) NOT NULL,
  `featureName` varchar(250) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `feature` */

insert  into `feature`(`id`,`featureName`) values 

(1,'mainScreen'),
(2,'transactionScreen');


/*Table structure for table `featureswitch` */

DROP TABLE IF EXISTS `featureswitch`;

CREATE TABLE `featureswitch` (
  `id` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `featureId` int(11) NOT NULL,
  `isEnable` boolean NOT NULL,
   PRIMARY KEY (`id`),
   FOREIGN KEY (userId) REFERENCES user(id),
   FOREIGN KEY (featureId) REFERENCES feature(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `featureswitch` */

insert  into `featureswitch`(`id`,`userId`,`featureId`,`isEnable`) values 

(1,1,1,1),
(2,2,2,1),
(3,2,1,1),
(4,1,2,1);






