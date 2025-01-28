import { env } from "process";
import ProductForm from "./productForm";

export default async function FormPage() {
  const dataManufacturer = await fetch(
    `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/manufacturer`,
    { cache: "force-cache" }
  );
  const manufacturers: ManufacturerInfo[] = await dataManufacturer.json();

  const dataSubcategory = await fetch(
    `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/subcategory`,
    { cache: "force-cache" }
  );
  const subcategories: SubcategoryInfo[] = await dataSubcategory.json();

  const dataWarehouse = await fetch(
    `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/warehouse`,
    { cache: "force-cache" }
  );
  const warehouses: Warehouse[] = await dataWarehouse.json();

  return (
    <div>
      <ProductForm
        manufacturerData={manufacturers}
        subcategoryData={subcategories}
        warehouseData={warehouses}
      />
    </div>
  );
}
