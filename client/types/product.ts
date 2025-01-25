type Product = {
    id: string;
    name: string;
    description: string;
    barcode: string;
    imageUrl: string;
    netPriceInCents: number;
    grossPriceInCents: number;
    stock: number;
    subcategory: SubcategoryInfo;
    manufacturer: ManufacturerInfo;
    warehouseId: string;
};
