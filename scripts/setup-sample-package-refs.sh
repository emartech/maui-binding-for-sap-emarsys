#!/bin/bash

set -e  # Exit on error

echo "====================================="
echo "Setting up Sample Apps Package References"
echo "====================================="

echo ""
echo "Updating Sample.csproj..."

# Ensure we're using PackageReference for package validation
sed -i '' 's|<ProjectReference Include="\\.\\.\\common\\Public\\Emarsys.Binding.csproj" />|<!-- <ProjectReference Include="..\\common\\Public\\Emarsys.Binding.csproj" /> -->|g' sample/Sample.csproj
sed -i '' 's|<!-- <PackageReference Include="Maui.Binding.SAP.Emarsys" Version="0.1.2" /> -->|<PackageReference Include="Maui.Binding.SAP.Emarsys" Version="0.1.2" />|g' sample/Sample.csproj

echo "✅ Sample.csproj updated to use PackageReference"

echo ""
echo "Updating Sample.NotificationService.csproj..."

# Ensure we're using PackageReference for package validation
sed -i '' 's|<ProjectReference Include="\\.\\.\\common\\Public\\Emarsys.Binding.csproj" />|<!-- <ProjectReference Include="..\\common\\Public\\Emarsys.Binding.csproj" /> -->|g' sample.NotificationService/Sample.NotificationService.csproj
sed -i '' 's|<!-- <PackageReference Include="Maui.Binding.SAP.Emarsys" Version="0.1.2" /> -->|<PackageReference Include="Maui.Binding.SAP.Emarsys" Version="0.1.2" />|g' sample.NotificationService/Sample.NotificationService.csproj

echo "✅ Sample.NotificationService.csproj updated to use PackageReference"

echo ""
echo "====================================="
echo "✅ Sample apps setup completed!"
echo "====================================="
