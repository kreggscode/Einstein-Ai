import json

# Read the JSON file
with open('app/src/main/assets/dataset1.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# Filter out header rows
cleaned_data = [
    item for item in data 
    if item.get('Number') != 'Number' and item.get('Category') != 'Category'
]

print(f"Original count: {len(data)}")
print(f"Cleaned count: {len(cleaned_data)}")
print(f"Removed {len(data) - len(cleaned_data)} header rows")

# Write back the cleaned data
with open('app/src/main/assets/dataset1.json', 'w', encoding='utf-8') as f:
    json.dump(cleaned_data, f, indent=2, ensure_ascii=False)

print("Dataset cleaned successfully!")
