Linux
=====

All Linux distributions are different and you may have to adapt the instructions here for your own distro.

Run Locally (development)
-------------------------

Assumes you are logged in as root.

Install dependencies.

For Debian and derivatives

    # apt-get install curl git build-essential
    # apt-get install build-essential bison openssl libreadline6 libreadline6-dev curl git-core zlib1g zlib1g-dev libssl-dev libyaml-dev libsqlite3-0 libsqlite3-dev sqlite3 libxml2-dev libxslt-dev autoconf

For Redhat and derivatives

    # yum install wget make autoconf automake gcc gcc-c++ patch bzip2 libtool readline readline-devel kernel-devel git bison autoconf sqlite-devel zlib-devel libxml2 libxml2-devel libxslt libxslt-devel libyaml libyaml-devel libffi-devel openssl-devel
    # yum install -y iconv-devel ; < This may not be required by your distro

Install RVM:

    # bash < <(curl -s https://rvm.beginrescueend.com/install/rvm)

Add root to the rvm group.

    # usermod -G rvm root

You may have to log out and log back in again for this to take effect.

Install Ruby 1.9.2-P136

    # rvm install 1.9.2-p136

Create user for provisioning portal

    # adduser abiquoprov

Add abiquoprov user to rvm group.

    # usermod -G rvm abiquoprov

Su to the new user.

    # su - abiquoprov

Set correct ruby version.

    $ rvm use 1.9.2-p136

Download and unpack provisioning portal code.

    $ wget http://download.abiquo.com/extras/abiquo-provisioning-portal-latest.tar.gz
    $ tar -xzvf abiquo-provisioning-portal-latest.tar.gz

Change to the project directory and accept the .rvmrc file.

    $ cd abiquo-provisioning-portal-{md5-hash}
    Do you wish to trust this .rvmrc file? (/home/abiquoprov/abiquo-provisioning-portal-d888c848cd88e20ed20d23d220ad645a95d9c27f/.rvmrc)
    y[es], n[o], v[iew], c[ancel]> y

Create and use a gemset for the project.

    $ rvm gemset create abiquoprov
    $ rvm use 1.9.2-p136@abiquoprov

Install bundler.

    $ gem install bundler

Install dependencies for the project.

    $ bundle install

If you get the following error:

    "NoMethodError: undefined method `write' for #<Syck::Emitter:0x88888888888888>
    An error occured while installing webee (0.3.6), and Bundler cannot continue.
    Make sure that `gem install webee -v '0.3.6'` succeeds before bundling."
    
Run bundler like this instead.

    $ RUBYOPT='-rpsych' bundle install

Now you should be able to load the database and start the project.

    $ rake db:load
    -- create_table("active_admin_comments", {:force=>true})
       -> 0.0253s
    ...
    $ rails server
    => Booting WEBrick
    => Rails 3.0.9 application starting in development on http://0.0.0.0:3000
    => Call with -d to detach
    => Ctrl-C to shutdown server
    [2011-09-30 10:57:56] INFO  WEBrick 1.3.1
    [2011-09-30 10:57:56] INFO  ruby 1.9.2 (2010-12-25) [x86_64-linux]
    [2011-09-30 10:57:56] INFO  WEBrick::HTTPServer#start: pid=28556 port=3000

You should now navigate to a working portal at http://localhost:3000

Deploy to Heroku
----------------

Heroku is a Platform as a Service designed specifically for Rails and Rack apps, although it has since expanded out to other systems. Heroku makes an excellent deployment platform.

Follow the steps above to install the dependencies for Ruby and RVM, and install the correct version of Ruby:

    $ sudo rvm install 1.9.2-p136

Download and unpack provisioning portal code.

    $ wget http://download.abiquo.com/extras/abiquo-provisioning-portal-latest.tar.gz
    $ tar -xzvf abiquo-provisioning-portal-latest.tar.gz

Change to the project directory and accept the .rvmrc file.

    $ cd abiquo-provisioning-portal-{md5-hash}
    Do you wish to trust this .rvmrc file? (/home/abiquoprov/abiquo-provisioning-portal-d888c848cd88e20ed20d23d220ad645a95d9c27f/.rvmrc)
    y[es], n[o], v[iew], c[ancel]> y
    
Install the Heroku gem.

    $ gem install heroku

Follow the [Heroku quick start guide](http://devcenter.heroku.com/articles/quickstart) to learn about setting up Heroku.

Add and commit the code to a new git repository.

    $ git init
    $ git add .
    $ git commit -m "Commit for Heroku"
    
Create a Heroku app and push the code to it.

    $ heroku create myprovisioningportal
    $ git push heroku master
    $ heroku rake db:load

Run Via Apache/Passenger
------------------------

This part assumes you have followed the steps to have the application running in local development mode above. We'll cover setting up PostgreSQL as the database server as well. You will need to modify the steps accordingly if you wish to use MySQL instead.

Install the dependencies.

Debian

    # apt-get install apache2 postgresql libpq-dev libcurl4-openssl-dev apache2-prefork-dev libapr1-dev libaprutil1-dev

Redhat

    # yum install apache2 postgresql < check this

Su to the postgres user

    # su - postgres

Create a database and a role. The options specify non-superuser, able to create databases and not able to create new roles. This will prompt for a password which you will need later.

    $ createuser -SdRP abiquoprov
    $ createdb abiquoprov

Exit the postgres user to go back to root

    $ exit

Change to the project directory.

    # cd /home/abiquoprov/{project directory}

We need to edit the Gemfile to add the 'pg' gem. Edit the file config/database.yml and add the following directly below the line `gem 'sqlite3'`

    gem 'pg'

Don't run `bundle install` just yet, we will do this later.

Edit the file `config/database.yml` to add the details for the database. Edit the production section to read like the following, substituting your own values where necessary. See the [Rails Database configuration guide](http://guides.rubyonrails.org/getting_started.html#configuring-a-database) for more details.

    production:
      adapter: postgresql
      host: localhost
      username: abiquoprov
      password: {password you entered above}
      database: abiquoprov
  
Now we should be able to load the database:

    # export RAILS_ENV=production && rake db:load

Install the passenger gem and run the installer. Follow the steps to set it up. For more information on Passenger, please see the [documentation](http://www.modrails.com/install.html).

    # gem install passenger
    # passenger-install-apache2-module

Once you have done all that you should be able to set up the site. Create a new virtualhost config (see your distro's instructions) and make sure it contains the following. As always, substitute your own values. You should point a unique DNS name at your server.

    <VirtualHost *:80>
    	ServerName abiquoprov.com
    	ServerAdmin webmaster@abiquo.com

    	DocumentRoot /path/to/project/public
    	<Directory /path/to/project/public>
    		AllowOverride all
    		Options -MultiViews
    	</Directory>
    	PassengerPoolIdleTime 0
    </VirtualHost>

Reload or restart Apache.

    # apachectl restart

We will need to run bundler again as Passenger will not pick up the gems from the RVM created gemset.

    # cd {path_to_project}
    # rvm use 1.9.2-p136
    # gem install bundler
    # bundle install

Now navigate to the application in your browser. In the case of the site config listed above, the URI would be http://abiquoprov.com