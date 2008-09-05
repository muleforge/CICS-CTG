require 'default.rb'
require 'defaultMappingRegistry.rb'
require 'soap/rpc/driver'

class MuleCicsProxy < ::SOAP::RPC::Driver
  DefaultEndpointUrl = "http://localhost:8888/seriola/CustomerInterface"

  Methods = [
    [ "",
      "sample_command",
      [ ["in", "sample-commandRequest", ["::SOAP::SOAPElement", "http://CustomerInterface/", "sample-command"]],
        ["out", "sample-commandResponse", ["::SOAP::SOAPElement", "http://CustomerInterface/", "sample-response"]] ],
      { :request_style =>  :document, :request_use =>  :literal,
        :response_style => :document, :response_use => :literal,
        :faults => {"SampleCommandFault"=>{:ns=>"http://CustomerInterface/", :use=>"literal", :namespace=>"http://CustomerInterface/", :name=>"sample-commandFault", :encodingstyle=>"http://schemas.xmlsoap.org/soap/encoding/"}} }
    ],
    [ "",
      "sample_duplicate_command",
      [ ["in", "sample-duplicate-commandRequest", ["::SOAP::SOAPElement", "http://CustomerInterface/", "sample-duplicate-command"]],
        ["out", "sample-duplicate-commandResponse", ["::SOAP::SOAPElement", "http://CustomerInterface/", "sample-response"]] ],
      { :request_style =>  :document, :request_use =>  :literal,
        :response_style => :document, :response_use => :literal,
        :faults => {"SampleDuplicateCommandFault"=>{:ns=>"http://CustomerInterface/", :use=>"literal", :namespace=>"http://CustomerInterface/", :name=>"sample-duplicate-commandFault", :encodingstyle=>"http://schemas.xmlsoap.org/soap/encoding/"}} }
    ],
    [ "",
      "sample_list_command",
      [ ["in", "sample-list-commandRequest", ["::SOAP::SOAPElement", "http://CustomerInterface/", "sample-list-command"]],
        ["out", "sample-list-commandResponse", ["::SOAP::SOAPElement", "http://CustomerInterface/", "sample-response"]] ],
      { :request_style =>  :document, :request_use =>  :literal,
        :response_style => :document, :response_use => :literal,
        :faults => {} }
    ]
  ]

  def initialize(endpoint_url = nil)
    endpoint_url ||= DefaultEndpointUrl
    super(endpoint_url, nil)
    self.mapping_registry = DefaultMappingRegistry::EncodedRegistry
    self.literal_mapping_registry = DefaultMappingRegistry::LiteralRegistry
    init_methods
  end

private

  def init_methods
    Methods.each do |definitions|
      opt = definitions.last
      if opt[:request_style] == :document
        add_document_operation(*definitions)
      else
        add_rpc_operation(*definitions)
        qname = definitions[0]
        name = definitions[2]
        if qname.name != name and qname.name.capitalize == name.capitalize
          ::SOAP::Mapping.define_singleton_method(self, qname.name) do |*arg|
            __send__(name, *arg)
          end
        end
      end
    end
  end
end

