require 'default.rb'
require 'soap/mapping'

module DefaultMappingRegistry
  EncodedRegistry = ::SOAP::Mapping::EncodedRegistry.new
  LiteralRegistry = ::SOAP::Mapping::LiteralRegistry.new
  NsCustomerInterface = "http://CustomerInterface/"

  EncodedRegistry.register(
    :class => SampleCommandType,
    :schema_type => XSD::QName.new(NsCustomerInterface, "sample-commandType"),
    :schema_element => [
      ["customer_info", ["SampleCommandType::CustomerInfo", XSD::QName.new(NsCustomerInterface, "customer-info")]]
    ]
  )

  EncodedRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerNo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-no"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerName,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-name"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerAddress,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-address"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleCommandType::CustomerInfo::SearchDate,
    :schema_name => XSD::QName.new(NsCustomerInterface, "search-date"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleCommandType::CustomerInfo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-info"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_element => [
      ["customer_no", ["SampleCommandType::CustomerInfo::CustomerNo", XSD::QName.new(NsCustomerInterface, "customer-no")]],
      ["customer_name", ["SampleCommandType::CustomerInfo::CustomerName", XSD::QName.new(NsCustomerInterface, "customer-name")]],
      ["customer_address", ["SampleCommandType::CustomerInfo::CustomerAddress", XSD::QName.new(NsCustomerInterface, "customer-address")]],
      ["search_date", ["SampleCommandType::CustomerInfo::SearchDate", XSD::QName.new(NsCustomerInterface, "search-date")]]
    ]
  )

  EncodedRegistry.register(
    :class => SampleResponseType,
    :schema_type => XSD::QName.new(NsCustomerInterface, "sample-responseType"),
    :schema_element => [
      ["no_of_records", ["SampleResponseType::NoOfRecords", XSD::QName.new(NsCustomerInterface, "no-of-records")]],
      ["search_date", ["SampleResponseType::SearchDate", XSD::QName.new(NsCustomerInterface, "search-date")]],
      ["customer_list", ["SampleResponseType::CustomerList[]", XSD::QName.new(NsCustomerInterface, "customer-list")], [5, 5]]
    ]
  )

  EncodedRegistry.register(
    :class => SampleResponseType::NoOfRecords,
    :schema_name => XSD::QName.new(NsCustomerInterface, "no-of-records"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleResponseType::SearchDate,
    :schema_name => XSD::QName.new(NsCustomerInterface, "search-date"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerNo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-no"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerName,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-name"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerAddress,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-address"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerAmount,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-amount"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerCharge,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-charge"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  EncodedRegistry.register(
    :class => SampleResponseType::CustomerList,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-list"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_element => [
      ["customer_no", ["SampleResponseType::CustomerList::CustomerNo", XSD::QName.new(NsCustomerInterface, "customer-no")]],
      ["customer_name", ["SampleResponseType::CustomerList::CustomerName", XSD::QName.new(NsCustomerInterface, "customer-name")]],
      ["customer_address", ["SampleResponseType::CustomerList::CustomerAddress", XSD::QName.new(NsCustomerInterface, "customer-address")]],
      ["customer_amount", ["SampleResponseType::CustomerList::CustomerAmount", XSD::QName.new(NsCustomerInterface, "customer-amount")]],
      ["customer_charge", ["SampleResponseType::CustomerList::CustomerCharge", XSD::QName.new(NsCustomerInterface, "customer-charge")]]
    ]
  )

  EncodedRegistry.register(
    :class => FaultDataType,
    :schema_type => XSD::QName.new(NsCustomerInterface, "fault-dataType"),
    :schema_element => [
      ["fault_class", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-class")]],
      ["fault_code", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-code")]],
      ["fault_message", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-message")]],
      ["fault_detail", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-detail")]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleCommandType,
    :schema_type => XSD::QName.new(NsCustomerInterface, "sample-commandType"),
    :schema_element => [
      ["customer_info", ["SampleCommandType::CustomerInfo", XSD::QName.new(NsCustomerInterface, "customer-info")]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerNo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-no"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerName,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-name"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerAddress,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-address"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::SearchDate,
    :schema_name => XSD::QName.new(NsCustomerInterface, "search-date"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-info"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_element => [
      ["customer_no", ["SampleCommandType::CustomerInfo::CustomerNo", XSD::QName.new(NsCustomerInterface, "customer-no")]],
      ["customer_name", ["SampleCommandType::CustomerInfo::CustomerName", XSD::QName.new(NsCustomerInterface, "customer-name")]],
      ["customer_address", ["SampleCommandType::CustomerInfo::CustomerAddress", XSD::QName.new(NsCustomerInterface, "customer-address")]],
      ["search_date", ["SampleCommandType::CustomerInfo::SearchDate", XSD::QName.new(NsCustomerInterface, "search-date")]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleResponseType,
    :schema_type => XSD::QName.new(NsCustomerInterface, "sample-responseType"),
    :schema_element => [
      ["no_of_records", ["SampleResponseType::NoOfRecords", XSD::QName.new(NsCustomerInterface, "no-of-records")]],
      ["search_date", ["SampleResponseType::SearchDate", XSD::QName.new(NsCustomerInterface, "search-date")]],
      ["customer_list", ["SampleResponseType::CustomerList[]", XSD::QName.new(NsCustomerInterface, "customer-list")], [5, 5]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleResponseType::NoOfRecords,
    :schema_name => XSD::QName.new(NsCustomerInterface, "no-of-records"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::SearchDate,
    :schema_name => XSD::QName.new(NsCustomerInterface, "search-date"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerNo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-no"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerName,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-name"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerAddress,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-address"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerAmount,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-amount"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerCharge,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-charge"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-list"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_element => [
      ["customer_no", ["SampleResponseType::CustomerList::CustomerNo", XSD::QName.new(NsCustomerInterface, "customer-no")]],
      ["customer_name", ["SampleResponseType::CustomerList::CustomerName", XSD::QName.new(NsCustomerInterface, "customer-name")]],
      ["customer_address", ["SampleResponseType::CustomerList::CustomerAddress", XSD::QName.new(NsCustomerInterface, "customer-address")]],
      ["customer_amount", ["SampleResponseType::CustomerList::CustomerAmount", XSD::QName.new(NsCustomerInterface, "customer-amount")]],
      ["customer_charge", ["SampleResponseType::CustomerList::CustomerCharge", XSD::QName.new(NsCustomerInterface, "customer-charge")]]
    ]
  )

  LiteralRegistry.register(
    :class => FaultDataType,
    :schema_type => XSD::QName.new(NsCustomerInterface, "fault-dataType"),
    :schema_element => [
      ["fault_class", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-class")]],
      ["fault_code", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-code")]],
      ["fault_message", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-message")]],
      ["fault_detail", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-detail")]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleCommandType,
    :schema_name => XSD::QName.new(NsCustomerInterface, "sample-command"),
    :schema_element => [
      ["customer_info", ["SampleCommandType::CustomerInfo", XSD::QName.new(NsCustomerInterface, "customer-info")]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerNo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-no"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerName,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-name"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerAddress,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-address"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::SearchDate,
    :schema_name => XSD::QName.new(NsCustomerInterface, "search-date"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-info"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_element => [
      ["customer_no", ["SampleCommandType::CustomerInfo::CustomerNo", XSD::QName.new(NsCustomerInterface, "customer-no")]],
      ["customer_name", ["SampleCommandType::CustomerInfo::CustomerName", XSD::QName.new(NsCustomerInterface, "customer-name")]],
      ["customer_address", ["SampleCommandType::CustomerInfo::CustomerAddress", XSD::QName.new(NsCustomerInterface, "customer-address")]],
      ["search_date", ["SampleCommandType::CustomerInfo::SearchDate", XSD::QName.new(NsCustomerInterface, "search-date")]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleResponseType,
    :schema_name => XSD::QName.new(NsCustomerInterface, "sample-response"),
    :schema_element => [
      ["no_of_records", ["SampleResponseType::NoOfRecords", XSD::QName.new(NsCustomerInterface, "no-of-records")]],
      ["search_date", ["SampleResponseType::SearchDate", XSD::QName.new(NsCustomerInterface, "search-date")]],
      ["customer_list", ["SampleResponseType::CustomerList[]", XSD::QName.new(NsCustomerInterface, "customer-list")], [5, 5]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleResponseType::NoOfRecords,
    :schema_name => XSD::QName.new(NsCustomerInterface, "no-of-records"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::SearchDate,
    :schema_name => XSD::QName.new(NsCustomerInterface, "search-date"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerNo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-no"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerName,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-name"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerAddress,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-address"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerAmount,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-amount"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList::CustomerCharge,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-charge"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleResponseType::CustomerList,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-list"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_element => [
      ["customer_no", ["SampleResponseType::CustomerList::CustomerNo", XSD::QName.new(NsCustomerInterface, "customer-no")]],
      ["customer_name", ["SampleResponseType::CustomerList::CustomerName", XSD::QName.new(NsCustomerInterface, "customer-name")]],
      ["customer_address", ["SampleResponseType::CustomerList::CustomerAddress", XSD::QName.new(NsCustomerInterface, "customer-address")]],
      ["customer_amount", ["SampleResponseType::CustomerList::CustomerAmount", XSD::QName.new(NsCustomerInterface, "customer-amount")]],
      ["customer_charge", ["SampleResponseType::CustomerList::CustomerCharge", XSD::QName.new(NsCustomerInterface, "customer-charge")]]
    ]
  )

  LiteralRegistry.register(
    :class => FaultDataType,
    :schema_name => XSD::QName.new(NsCustomerInterface, "fault-data"),
    :schema_element => [
      ["fault_class", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-class")]],
      ["fault_code", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-code")]],
      ["fault_message", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-message")]],
      ["fault_detail", ["SOAP::SOAPString", XSD::QName.new(NsCustomerInterface, "fault-detail")]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleCommandType,
    :schema_name => XSD::QName.new(NsCustomerInterface, "sample-duplicate-command"),
    :schema_element => [
      ["customer_info", ["SampleCommandType::CustomerInfo", XSD::QName.new(NsCustomerInterface, "customer-info")]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerNo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-no"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerName,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-name"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerAddress,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-address"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::SearchDate,
    :schema_name => XSD::QName.new(NsCustomerInterface, "search-date"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-info"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_element => [
      ["customer_no", ["SampleCommandType::CustomerInfo::CustomerNo", XSD::QName.new(NsCustomerInterface, "customer-no")]],
      ["customer_name", ["SampleCommandType::CustomerInfo::CustomerName", XSD::QName.new(NsCustomerInterface, "customer-name")]],
      ["customer_address", ["SampleCommandType::CustomerInfo::CustomerAddress", XSD::QName.new(NsCustomerInterface, "customer-address")]],
      ["search_date", ["SampleCommandType::CustomerInfo::SearchDate", XSD::QName.new(NsCustomerInterface, "search-date")]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleCommandType,
    :schema_name => XSD::QName.new(NsCustomerInterface, "sample-list-command"),
    :schema_element => [
      ["customer_info", ["SampleCommandType::CustomerInfo", XSD::QName.new(NsCustomerInterface, "customer-info")]]
    ]
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerNo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-no"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerName,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-name"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::CustomerAddress,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-address"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo::SearchDate,
    :schema_name => XSD::QName.new(NsCustomerInterface, "search-date"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_attribute => {
      XSD::QName.new(nil, "type") => "SOAP::SOAPString",
      XSD::QName.new(nil, "length") => "SOAP::SOAPString"
    }
  )

  LiteralRegistry.register(
    :class => SampleCommandType::CustomerInfo,
    :schema_name => XSD::QName.new(NsCustomerInterface, "customer-info"),
    :is_anonymous => true,
    :schema_qualified => true,
    :schema_element => [
      ["customer_no", ["SampleCommandType::CustomerInfo::CustomerNo", XSD::QName.new(NsCustomerInterface, "customer-no")]],
      ["customer_name", ["SampleCommandType::CustomerInfo::CustomerName", XSD::QName.new(NsCustomerInterface, "customer-name")]],
      ["customer_address", ["SampleCommandType::CustomerInfo::CustomerAddress", XSD::QName.new(NsCustomerInterface, "customer-address")]],
      ["search_date", ["SampleCommandType::CustomerInfo::SearchDate", XSD::QName.new(NsCustomerInterface, "search-date")]]
    ]
  )
end
