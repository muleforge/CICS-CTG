require 'wadl'

customerInterface = WADL::Application.from_wadl(open("CustomerInterface.wadl"))

expected_representation = customerInterface.sample_command.find_method(:sampleCommand).response.representations[0]
query = {:customer_no => "1111", :cutomer_name => "‚¢‚¢‚¢‚¢‚¢‚¢‚¢",:customer_address => "‚ ‚ ‚ ‚ ‚ ‚ ‚ ", :search_date => "20060701" }

# Args:
  #  :path - Values for path parameters
  #  :query - Values for query parameters
  #  :headers - Values for header parameters
  #  :send_representation
  #  :expect_representation
hash = {}
hash['path'] = "/customerInterface"
hash['query'] = "/customerInterface/sample-command?customer-name=abc"
hash['headers'] = ""
hash['send_representation'] = customerInterface.sample_command.find_method(:sampleCommand).request
hash['expect_representation'] = expected_representation

result = customerInterface.sample_command.find_method(:sampleCommand).call( customerInterface.sample_command  , hash )
puts result.representation
