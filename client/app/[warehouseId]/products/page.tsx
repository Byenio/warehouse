import { env } from "process";
import FilterableProducts from "./filterable-products";

export default async function Products({
  params,
}: {
  params: Promise<{ warehouseId: string }>;
}) {
  const { warehouseId } = await params;

  const data = await fetch(
    `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/product`
  );
  const allProducts: Product[] = await data.json();

  const products: Product[] = allProducts.filter(
    (product) => product.warehouseId === warehouseId
  );

  return (
    <>
      <FilterableProducts products={products} />
    </>
  );
}
