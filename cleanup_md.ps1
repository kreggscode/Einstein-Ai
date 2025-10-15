# Remove extra markdown files and update .gitignore
Remove-Item "COPY_PASTE_GUIDE.md" -Force
Remove-Item "IMPORTANT_READ_THIS.md" -Force

# Update .gitignore to exclude markdown files except README.md
Add-Content ".gitignore" "`n# Exclude markdown files except README.md`n*.md`n!README.md"

Write-Host "Removed extra markdown files"
Write-Host "Updated .gitignore to exclude *.md except README.md"

# Commit changes
git add .
git commit -m "Remove extra markdown files, keep only README.md"
git push

Write-Host "`nPushed to GitHub - only README.md remains!"

# Self-delete
Remove-Item "cleanup_md.ps1" -Force
