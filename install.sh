#!/bin/bash

EXPECTED_ARGS=4
E_BADARGS=65

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Usage: $0 dbname dbuser dbpass portalDir"
  exit $E_BADARGS
fi

#installing play
#wget http://download.playframework.org/releases/play-1.2.4.zip
#unzip play-1.2.4.zip -d /opt/play-1.2
#export PATH=$PATH:/opt/play-1.2

#creating database schema
MYSQL=`which mysql` 
Q1="CREATE DATABASE IF NOT EXISTS $1;"
Q2="GRANT ALL ON $1.* TO '$2'@'localhost' IDENTIFIED BY '$3';"
Q3="FLUSH PRIVILEGES;"
SQL="${Q1}${Q2}${Q3}" 
sudo $MYSQL -u $2 -p$3 -e "$SQL"

#configuring database and dependencies 
PLAY=`which play`
cd $4
sudo sed -i 's/jpa.ddl=update/jpa.ddl=create/g' $4/conf/application.conf
sudo $PLAY deps --sync 
sudo $PLAY run && sudo $PLAY stop
sudo sed -i 's/jpa.ddl=create/jpa.ddl=update/g' $4/conf/application.conf

#war creation
#sudo play war $4 -o $5/webapps/ProvisioningPortal --zip
#sudo zip -d $5/webapps/ProvisioningPortal.war /WEB-INF/lib/gson-1.7.1.jar
#sudo rm $5/webapps/ProvisioningPortal/WEB-INF/lib/gson-1.7.1.jar

#executing war container
#sudo $5/bin/catalina.sh run
