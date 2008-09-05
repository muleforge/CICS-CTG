#!/usr/bin/env ruby
require 'defaultDriver.rb'

endpoint_url = ARGV.shift
obj = MuleCicsProxy.new(endpoint_url)

# run ruby with -d to see SOAP wiredumps.
obj.wiredump_dev = STDERR if $DEBUG

# SYNOPSIS
#   sample-command(sample_commandRequest)
#
# ARGS
#   sample_commandRequest SampleCommandType - {http://CustomerInterface/}sample-commandType
#
# RETURNS
#   sample_commandResponse SampleResponseType - {http://CustomerInterface/}sample-responseType
#
# RAISES
#   sample_commandFault FaultDataType - {http://CustomerInterface/}fault-dataType
#
puts "Executing sample-command..."

sample_commandRequest = SampleCommandType.new
custNo=SampleCommandType::CustomerInfo::CustomerNo.new("1111")
custName=SampleCommandType::CustomerInfo::CustomerName.new("abc")
custAddr=SampleCommandType::CustomerInfo::CustomerAddress.new("xyz")
searchDate=SampleCommandType::CustomerInfo::SearchDate.new("20060701")

custInfo= SampleCommandType::CustomerInfo.new()
custInfo.customer_no=custNo
custInfo.customer_name=custName
custInfo.customer_address=custAddr
custInfo.search_date=searchDate

sample_commandRequest.customer_info = custInfo
sampleResposne = obj.sample_command(sample_commandRequest)
custList = sampleResposne.customer_list
count=0
while count < custList.size
    puts "Customer No: "+custList[count].customer_no.to_s
    #strObj=Chilkat::CkString.new(custList[count].customer_name.to_s)
    puts "Customer Name: "+custList[count].customer_name.to_s
    puts "Address: "+custList[count].customer_address.to_s
    puts "Amount: "+custList[count].customer_amount.to_s
    puts "Charge: "+custList[count].customer_charge.to_s
    puts
    count+=1
end

puts "---------------------------------------------------"
# SYNOPSIS
#   sample-duplicate-command(sample_duplicate_commandRequest)
#
# ARGS
#   sample_duplicate_commandRequest SampleCommandType - {http://CustomerInterface/}sample-commandType
#
# RETURNS
#   sample_duplicate_commandResponse SampleResponseType - {http://CustomerInterface/}sample-responseType
#
# RAISES
#   sample_duplicate_commandFault FaultDataType - {http://CustomerInterface/}fault-dataType
#
#sample_duplicate_commandRequest = nil
#puts obj.sample_duplicate_command(sample_duplicate_commandRequest)
puts "Executing sample_duplicate_command..."

sample_commandRequest = SampleCommandType.new
custNo=SampleCommandType::CustomerInfo::CustomerNo.new("1111")
custName=SampleCommandType::CustomerInfo::CustomerName.new("abc")
custAddr=SampleCommandType::CustomerInfo::CustomerAddress.new("xyz")
searchDate=SampleCommandType::CustomerInfo::SearchDate.new("20060701")

custInfo= SampleCommandType::CustomerInfo.new()
custInfo.customer_no=custNo
custInfo.customer_name=custName
custInfo.customer_address=custAddr
custInfo.search_date=searchDate

sample_commandRequest.customer_info = custInfo
sampleResposne = obj.sample_duplicate_command(sample_commandRequest)
custList = sampleResposne.customer_list
count=0
while count < custList.size
    puts "Customer No: "+custList[count].customer_no.to_s
    #strObj=Chilkat::CkString.new(custList[count].customer_name.to_s)
    puts "Customer Name: "+custList[count].customer_name.to_s
    puts "Address: "+custList[count].customer_address.to_s
    puts "Amount: "+custList[count].customer_amount.to_s
    puts "Charge: "+custList[count].customer_charge.to_s
    puts
    count+=1
end

puts "---------------------------------------------------"

# SYNOPSIS
#   sample-list-command(sample_list_commandRequest)
#
# ARGS
#   sample_list_commandRequest SampleCommandType - {http://CustomerInterface/}sample-commandType
#
# RETURNS
#   sample_list_commandResponse SampleResponseType - {http://CustomerInterface/}sample-responseType
#
#sample_list_commandRequest = nil
#puts obj.sample_list_command(sample_list_commandRequest)

puts "Executing sample_list_command..."

sample_commandRequest = SampleCommandType.new
custNo=SampleCommandType::CustomerInfo::CustomerNo.new("1111")
custName=SampleCommandType::CustomerInfo::CustomerName.new("abc")
custAddr=SampleCommandType::CustomerInfo::CustomerAddress.new("xyz")
searchDate=SampleCommandType::CustomerInfo::SearchDate.new("20060701")

custInfo= SampleCommandType::CustomerInfo.new()
custInfo.customer_no=custNo
custInfo.customer_name=custName
custInfo.customer_address=custAddr
custInfo.search_date=searchDate

sample_commandRequest.customer_info = custInfo
sampleResposne = obj.sample_list_command(sample_commandRequest)
custList = sampleResposne.customer_list
count=0
while count < custList.size
    puts "Customer No: "+custList[count].customer_no.to_s
    #strObj=Chilkat::CkString.new(custList[count].customer_name.to_s)
    puts "Customer Name: "+custList[count].customer_name.to_s
    puts "Address: "+custList[count].customer_address.to_s
    puts "Amount: "+custList[count].customer_amount.to_s
    puts "Charge: "+custList[count].customer_charge.to_s
    puts
    count+=1
end

puts "---------------------------------------------------"

