Installation of Ruby with soap4r library 
----------------------------------------

 1. Install ruby on your machine.
     a.Download ruby installable from http://www2.ruby-lang.org/en/20020102.html.	
 
 2. Install SOAP4R on your machine.
    You can either install soap4r manually or online using gems.

    I. Install soap4r manually
    --------------------------
    a. Download http://dev.ctor.org/download/soap4r-1.5.8.tar.gz.
    b.Run following commands:

      ${ruby}>tar -xvf soap4r-1.5.8.tar.gz
      ${ruby}>cd soap4r-1.5.8
      ${ruby}/soap4r-1.5.8>ruby install.rb

    II. Install soap4r using gems
    -----------------------------
    a. Run the following command
      >gems install soap4r 

Generating client-stub for mule-cics 
-------------------------------------
  1. Start the mule server.

  2. Generate the client-stubs.
     ${ruby-soap-client.home}/src >ruby wsdl2ruby.rb --wsdl http://localhost:8888/seriola/CustomerInterface --type client
     
     (The above command will create defaultDriver.rb,defaultMappingRegistry.rb,default.rb,
     <ServiceName>Client.rb in  ${ruby-soap-client.home}/src directory.)

Steps to run the client:
-----------------------

 1. Run ruby SOAP client.

    ${ruby-soap-client.home}/src>ruby SeriolaUMOClient.rb
