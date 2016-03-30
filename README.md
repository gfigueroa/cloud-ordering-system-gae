Cloud Ordering System
===============================================================================

## Description

Cloud Ordering and Smart TV System
Online shopping and ordering system where stores and users can register.
Works for stores, restaurants, shops, and individual owners.
Also contains Smart TV channels and listings with video and audio file upload.
Contains three interfaces: Admin, Store/Restaurant/Channel, Customer.
The system is developed for Google App Engine using Java.
It has a basic web interface and a web service API.

Can be accessed at: [link](http://smasrv-cos.appspot.com/)

### Related Projects

## Acknowledgements

Engineers and designers at SMASRV

## Directory Structure

    smasrv-cos/
        src/
			datastore/
			exceptions/
			META-INF/
			servlets/
			session/
			util/
			webbrowser/
			webservices/
		war/
			admin/
			customer/
			header/
			images/
			js/
			menu/
			restaurant/
			Scripts/
			stylesheets/
			testing/
			webbrowser/
			WEB-INF/

### src

All Java packages and source code goes here.

#### datastore

Datastore tables and table managers (CRUD operations).

#### exceptions

Specific exception classes.

#### META-INF

JDO configuration files.

#### servlets

All HTTP servlet implementations.

#### util

Various utilities.

#### webservices

Web service classes and routes file.

### war

Web elements.

## Authors

* [Smart Personalized Service Technology Inc.](http://www.smasrv.com)
 * Gerardo Figueroa, gfigueroa@smasrv.com