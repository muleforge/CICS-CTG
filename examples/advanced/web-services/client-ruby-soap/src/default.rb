require 'xsd/qname'

# {http://CustomerInterface/}sample-commandType
#   customer_info - SampleCommandType::CustomerInfo
class SampleCommandType

  # inner class for member: customer-info
  # {http://CustomerInterface/}customer-info
  #   customer_no - SampleCommandType::CustomerInfo::CustomerNo
  #   customer_name - SampleCommandType::CustomerInfo::CustomerName
  #   customer_address - SampleCommandType::CustomerInfo::CustomerAddress
  #   search_date - SampleCommandType::CustomerInfo::SearchDate
  class CustomerInfo

    # inner class for member: customer-no
    # {http://CustomerInterface/}customer-no
    #   xmlattr_type - SOAP::SOAPString
    #   xmlattr_length - SOAP::SOAPString
    class CustomerNo < ::String
      AttrLength = XSD::QName.new(nil, "length")
      AttrType = XSD::QName.new(nil, "type")

      def __xmlattr
        @__xmlattr ||= {}
      end

      def xmlattr_type
        __xmlattr[AttrType]
      end

      def xmlattr_type=(value)
        __xmlattr[AttrType] = value
      end

      def xmlattr_length
        __xmlattr[AttrLength]
      end

      def xmlattr_length=(value)
        __xmlattr[AttrLength] = value
      end

      def initialize(*arg)
        super
        @__xmlattr = {}
      end
    end

    # inner class for member: customer-name
    # {http://CustomerInterface/}customer-name
    #   xmlattr_type - SOAP::SOAPString
    #   xmlattr_length - SOAP::SOAPString
    class CustomerName < ::String
      AttrLength = XSD::QName.new(nil, "length")
      AttrType = XSD::QName.new(nil, "type")

      def __xmlattr
        @__xmlattr ||= {}
      end

      def xmlattr_type
        __xmlattr[AttrType]
      end

      def xmlattr_type=(value)
        __xmlattr[AttrType] = value
      end

      def xmlattr_length
        __xmlattr[AttrLength]
      end

      def xmlattr_length=(value)
        __xmlattr[AttrLength] = value
      end

      def initialize(*arg)
        super
        @__xmlattr = {}
      end
    end

    # inner class for member: customer-address
    # {http://CustomerInterface/}customer-address
    #   xmlattr_type - SOAP::SOAPString
    #   xmlattr_length - SOAP::SOAPString
    class CustomerAddress < ::String
      AttrLength = XSD::QName.new(nil, "length")
      AttrType = XSD::QName.new(nil, "type")

      def __xmlattr
        @__xmlattr ||= {}
      end

      def xmlattr_type
        __xmlattr[AttrType]
      end

      def xmlattr_type=(value)
        __xmlattr[AttrType] = value
      end

      def xmlattr_length
        __xmlattr[AttrLength]
      end

      def xmlattr_length=(value)
        __xmlattr[AttrLength] = value
      end

      def initialize(*arg)
        super
        @__xmlattr = {}
      end
    end

    # inner class for member: search-date
    # {http://CustomerInterface/}search-date
    #   xmlattr_type - SOAP::SOAPString
    #   xmlattr_length - SOAP::SOAPString
    class SearchDate < ::String
      AttrLength = XSD::QName.new(nil, "length")
      AttrType = XSD::QName.new(nil, "type")

      def __xmlattr
        @__xmlattr ||= {}
      end

      def xmlattr_type
        __xmlattr[AttrType]
      end

      def xmlattr_type=(value)
        __xmlattr[AttrType] = value
      end

      def xmlattr_length
        __xmlattr[AttrLength]
      end

      def xmlattr_length=(value)
        __xmlattr[AttrLength] = value
      end

      def initialize(*arg)
        super
        @__xmlattr = {}
      end
    end

    attr_accessor :customer_no
    attr_accessor :customer_name
    attr_accessor :customer_address
    attr_accessor :search_date

    def initialize(customer_no = nil, customer_name = nil, customer_address = nil, search_date = nil)
      @customer_no = customer_no
      @customer_name = customer_name
      @customer_address = customer_address
      @search_date = search_date
    end
  end

  attr_accessor :customer_info

  def initialize(customer_info = nil)
    @customer_info = customer_info
  end
end

# {http://CustomerInterface/}sample-responseType
#   no_of_records - SampleResponseType::NoOfRecords
#   search_date - SampleResponseType::SearchDate
#   customer_list - SampleResponseType::CustomerList
class SampleResponseType

  # inner class for member: no-of-records
  # {http://CustomerInterface/}no-of-records
  #   xmlattr_type - SOAP::SOAPString
  #   xmlattr_length - SOAP::SOAPString
  class NoOfRecords < ::String
    AttrLength = XSD::QName.new(nil, "length")
    AttrType = XSD::QName.new(nil, "type")

    def __xmlattr
      @__xmlattr ||= {}
    end

    def xmlattr_type
      __xmlattr[AttrType]
    end

    def xmlattr_type=(value)
      __xmlattr[AttrType] = value
    end

    def xmlattr_length
      __xmlattr[AttrLength]
    end

    def xmlattr_length=(value)
      __xmlattr[AttrLength] = value
    end

    def initialize(*arg)
      super
      @__xmlattr = {}
    end
  end

  # inner class for member: search-date
  # {http://CustomerInterface/}search-date
  #   xmlattr_type - SOAP::SOAPString
  #   xmlattr_length - SOAP::SOAPString
  class SearchDate < ::String
    AttrLength = XSD::QName.new(nil, "length")
    AttrType = XSD::QName.new(nil, "type")

    def __xmlattr
      @__xmlattr ||= {}
    end

    def xmlattr_type
      __xmlattr[AttrType]
    end

    def xmlattr_type=(value)
      __xmlattr[AttrType] = value
    end

    def xmlattr_length
      __xmlattr[AttrLength]
    end

    def xmlattr_length=(value)
      __xmlattr[AttrLength] = value
    end

    def initialize(*arg)
      super
      @__xmlattr = {}
    end
  end

  # inner class for member: customer-list
  # {http://CustomerInterface/}customer-list
  #   customer_no - SampleResponseType::CustomerList::CustomerNo
  #   customer_name - SampleResponseType::CustomerList::CustomerName
  #   customer_address - SampleResponseType::CustomerList::CustomerAddress
  #   customer_amount - SampleResponseType::CustomerList::CustomerAmount
  #   customer_charge - SampleResponseType::CustomerList::CustomerCharge
  class CustomerList

    # inner class for member: customer-no
    # {http://CustomerInterface/}customer-no
    #   xmlattr_type - SOAP::SOAPString
    #   xmlattr_length - SOAP::SOAPString
    class CustomerNo < ::String
      AttrLength = XSD::QName.new(nil, "length")
      AttrType = XSD::QName.new(nil, "type")

      def __xmlattr
        @__xmlattr ||= {}
      end

      def xmlattr_type
        __xmlattr[AttrType]
      end

      def xmlattr_type=(value)
        __xmlattr[AttrType] = value
      end

      def xmlattr_length
        __xmlattr[AttrLength]
      end

      def xmlattr_length=(value)
        __xmlattr[AttrLength] = value
      end

      def initialize(*arg)
        super
        @__xmlattr = {}
      end
    end

    # inner class for member: customer-name
    # {http://CustomerInterface/}customer-name
    #   xmlattr_type - SOAP::SOAPString
    #   xmlattr_length - SOAP::SOAPString
    class CustomerName < ::String
      AttrLength = XSD::QName.new(nil, "length")
      AttrType = XSD::QName.new(nil, "type")

      def __xmlattr
        @__xmlattr ||= {}
      end

      def xmlattr_type
        __xmlattr[AttrType]
      end

      def xmlattr_type=(value)
        __xmlattr[AttrType] = value
      end

      def xmlattr_length
        __xmlattr[AttrLength]
      end

      def xmlattr_length=(value)
        __xmlattr[AttrLength] = value
      end

      def initialize(*arg)
        super
        @__xmlattr = {}
      end
    end

    # inner class for member: customer-address
    # {http://CustomerInterface/}customer-address
    #   xmlattr_type - SOAP::SOAPString
    #   xmlattr_length - SOAP::SOAPString
    class CustomerAddress < ::String
      AttrLength = XSD::QName.new(nil, "length")
      AttrType = XSD::QName.new(nil, "type")

      def __xmlattr
        @__xmlattr ||= {}
      end

      def xmlattr_type
        __xmlattr[AttrType]
      end

      def xmlattr_type=(value)
        __xmlattr[AttrType] = value
      end

      def xmlattr_length
        __xmlattr[AttrLength]
      end

      def xmlattr_length=(value)
        __xmlattr[AttrLength] = value
      end

      def initialize(*arg)
        super
        @__xmlattr = {}
      end
    end

    # inner class for member: customer-amount
    # {http://CustomerInterface/}customer-amount
    #   xmlattr_type - SOAP::SOAPString
    #   xmlattr_length - SOAP::SOAPString
    class CustomerAmount < ::String
      AttrLength = XSD::QName.new(nil, "length")
      AttrType = XSD::QName.new(nil, "type")

      def __xmlattr
        @__xmlattr ||= {}
      end

      def xmlattr_type
        __xmlattr[AttrType]
      end

      def xmlattr_type=(value)
        __xmlattr[AttrType] = value
      end

      def xmlattr_length
        __xmlattr[AttrLength]
      end

      def xmlattr_length=(value)
        __xmlattr[AttrLength] = value
      end

      def initialize(*arg)
        super
        @__xmlattr = {}
      end
    end

    # inner class for member: customer-charge
    # {http://CustomerInterface/}customer-charge
    #   xmlattr_type - SOAP::SOAPString
    #   xmlattr_length - SOAP::SOAPString
    class CustomerCharge < ::String
      AttrLength = XSD::QName.new(nil, "length")
      AttrType = XSD::QName.new(nil, "type")

      def __xmlattr
        @__xmlattr ||= {}
      end

      def xmlattr_type
        __xmlattr[AttrType]
      end

      def xmlattr_type=(value)
        __xmlattr[AttrType] = value
      end

      def xmlattr_length
        __xmlattr[AttrLength]
      end

      def xmlattr_length=(value)
        __xmlattr[AttrLength] = value
      end

      def initialize(*arg)
        super
        @__xmlattr = {}
      end
    end

    attr_accessor :customer_no
    attr_accessor :customer_name
    attr_accessor :customer_address
    attr_accessor :customer_amount
    attr_accessor :customer_charge

    def initialize(customer_no = nil, customer_name = nil, customer_address = nil, customer_amount = nil, customer_charge = nil)
      @customer_no = customer_no
      @customer_name = customer_name
      @customer_address = customer_address
      @customer_amount = customer_amount
      @customer_charge = customer_charge
    end
  end

  attr_accessor :no_of_records
  attr_accessor :search_date
  attr_accessor :customer_list

  def initialize(no_of_records = nil, search_date = nil, customer_list = [])
    @no_of_records = no_of_records
    @search_date = search_date
    @customer_list = customer_list
  end
end

# {http://CustomerInterface/}fault-dataType
#   fault_class - SOAP::SOAPString
#   fault_code - SOAP::SOAPString
#   fault_message - SOAP::SOAPString
#   fault_detail - SOAP::SOAPString
class FaultDataType
  attr_accessor :fault_class
  attr_accessor :fault_code
  attr_accessor :fault_message
  attr_accessor :fault_detail

  def initialize(fault_class = nil, fault_code = nil, fault_message = nil, fault_detail = nil)
    @fault_class = fault_class
    @fault_code = fault_code
    @fault_message = fault_message
    @fault_detail = fault_detail
  end
end
