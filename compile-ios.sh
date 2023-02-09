
cd prebuilt

xcodebuild build -scheme Attributes -configuration Release -arch arm64 -sdk 'iphoneos' \
  BUILD_LIBRARY_FOR_DISTRIBUTION=YES \
  -derivedDataPath './Attributes/build/' \
  -project Attributes/Attributes.xcodeproj
cp Attributes/build/Build/Products/Release-iphoneos/libAttributes.a Attributes/binaries/arm64
rm -rf Attributes/build

xcodebuild build -scheme Attributes -configuration Release -arch arm64 -sdk 'iphonesimulator' \
  BUILD_LIBRARY_FOR_DISTRIBUTION=YES \
  -derivedDataPath './Attributes/build/' \
  -project Attributes/Attributes.xcodeproj
cp Attributes/build/Build/Products/Release-iphonesimulator/libAttributes.a Attributes/binaries/arm64-simulator
rm -rf Attributes/build

xcodebuild build -scheme Attributes -configuration Release -arch x86_64 -sdk 'iphonesimulator' \
  BUILD_LIBRARY_FOR_DISTRIBUTION=YES \
  -derivedDataPath './Attributes/build/' \
  -project Attributes/Attributes.xcodeproj
cp Attributes/build/Build/Products/Release-iphonesimulator/libAttributes.a Attributes/binaries/x64
rm -rf Attributes/build
