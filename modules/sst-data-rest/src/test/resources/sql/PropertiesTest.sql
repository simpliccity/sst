CREATE TABLE sst_properties (property_name VARCHAR(100) NOT NULL, property_value VARCHAR(200) NULL, PRIMARY KEY (property_name));
		 
INSERT INTO sst_properties (property_name, property_value) VALUES ('user1.letters', 'abcdefg'), ('user1.url', 'http://www.simpliccity.org'), ('user2.date', '2016-05-09');

