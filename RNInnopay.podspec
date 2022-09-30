require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = "RNInnopay"
  s.version      = package['version']
  s.summary      = "RNInnopay"
  s.description  = <<-DESC
                  RNInnopay
                   DESC
  s.homepage     = "appltree.io"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "syyoon@appltree.io" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/appltree/innopay.git", :tag => "master" }
  s.source_files  = "ios/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  