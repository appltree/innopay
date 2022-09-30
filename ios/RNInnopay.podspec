
Pod::Spec.new do |s|
  s.name         = "RNInnopay"
  s.version      = "1.0.0"
  s.summary      = "RNInnopay"
  s.description  = <<-DESC
                  RNInnopay
                   DESC
  s.homepage     = "apptree.io"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNInnopay.git", :tag => "master" }
  s.source_files  = "RNInnopay/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  