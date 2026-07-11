// swift-tools-version: 5.9

import PackageDescription

let package = Package(
    name: "Attributes",
    platforms: [
        .iOS(.v15)
    ],
    products: [
        .library(
            name: "Attributes",
            targets: ["Attributes"]
        )
    ],
    targets: [
        .target(
            name: "Attributes"
        )
    ]
)
